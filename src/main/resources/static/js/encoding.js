



// 합금철 투입량 표 생성 함수
function createAlloyTable(data) {
    const table = document.createElement("table");
    table.classList.add("table");

    for (const row of data) {

        const tableRow = document.createElement("tr");

        for (const cellData of row) {
            const cell = document.createElement("td");
            cell.textContent = cellData;
            tableRow.appendChild(cell);
        }

        table.appendChild(tableRow);
    }

    return table;
}

// 미리보기 테이블 생성 함수
function createPreviewTable(data, titleText) {
    const container = document.createElement("div");

    // 제목 추가
    const title = document.createElement("div");
    title.classList.add("title");
    title.textContent = titleText;
    container.appendChild(title);

    // 테이블 생성
    const table = createAlloyTable(data);
    const tableContainer = document.createElement("div");
    tableContainer.classList.add("table-responsive");
    tableContainer.appendChild(table);

    container.appendChild(tableContainer);

    return container;
}


let fileTitle;
//엑셀, csv파일만 선택가능 펑션
function validateFileType() {

    const allowedFileTypes = [
        'application/vnd.ms-excel', // XLS (이전 Excel 형식)
        'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet', // XLSX (Excel 2007 이상 형식)
        'text/csv', // CSV (쉼표로 구분된 값)
        'application/haansoftxlsx', // HWP XLSX (한글 문서)
    ];
    const fileInput = document.getElementById('changeFileInput');
    const files = fileInput.files;

    for (let i = 0; i < files.length; i++) {
        const fileType = files[i].type;
        fileTitle = files[i].name;
        console.log(fileTitle);
        console.log(files[i].type);
        if (!allowedFileTypes.includes(fileType)) {
            alert('선택한 파일 형식이 올바르지 않습니다. CSV 또는 Excel 파일만 선택해 주세요.');
            fileInput.value = "";  // 파일 입력 초기화
            return;
        }
    }
    return files[0];
}



let resultMode;
let decodingKeyList;
function sendExcelCsv(){

    const fileInput = document.getElementById("changeFileInput");
    const radios = document.getElementsByName("choiceRadio");
    let mode;


    radios.forEach(radio => {
        if (radio.checked) mode = radio.id;
    });

    if (!fileInput.files.length) {
        alert("파일을 선택하세요.");
        return;
    }

    const formData = new FormData();
    formData.append("file", fileInput.files[0]);
    formData.append("mode", mode);

    fetch("/sendExcelCsv", {
        method: "POST",
        body: formData
    })
        .then(response => response.json())
        .then(result => {
            const data = result.data;
            const data1 = result.data.slice();
            const data2 = data1.splice(data1.length - 3, 3);;
            const mode = result.mode;
            const decodingKey = data[7];

            decodingKeyList = decodingKey;


            // JSON 데이터를 2차원 배열로 변환
            const dataArray = data1.map(row => Object.values(row));

            // 표를 추가할 컨테이너 요소
            const container = document.querySelector(".card-table");

            // 미리보기 테이블 생성
            const previewTable = createPreviewTable(dataArray);
            container.appendChild(previewTable);

            resultData = data;
            resultMode = mode;
        })
        .catch(err => {
            console.error(err);
        });
    closeChoiceFileModal();
}


function changeDbSave() {

    console.log("세이브");

    //decodingKeySave();
    console.log(resultData);
    const resultModeDataList = { mode: resultMode, keyData: resultData }
        $.ajax({
            url: "/saveData",
            type: "POST",
            contentType: "application/json;charset=UTF-8",
            data: JSON.stringify(resultModeDataList),
        });
    alert("저장되었습니다.");
}
function csvOrExcelDownload(){
    // 날짜 포맷 지정 YYYY-MM-DD
    const date = new Date();
    const dateStr =
        date.getFullYear() +
        "-" +
        ("0" + (date.getMonth() + 1)).slice(-2) +
        "-" +
        ("0" + date.getDate()).slice(-2);

    var resultModeName;

    if(resultMode == "standardizationRadio"){
        resultModeName = "표준화";
    }else if(resultMode == "normalizationRadio"){
        resultModeName = "정규화";
    }

    // 파일 이름 설정
    const fileName = dateStr + "(" + fileTitle.slice(0, fileTitle.lastIndexOf(".")) + "(" + resultModeName + "변환)" + ")";
    const contentType =
        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8";
    const excelData = arrayToExcel(resultData);

    downloadFile(fileName + ".xlsx", excelData, contentType);

    console.log(resultData);
}

function downloadFile(filename, content, contentType) {
    const blob = new Blob([content], { type: contentType });
    const link = document.createElement("a");
    link.href = URL.createObjectURL(blob);
    link.download = filename;
    link.click();
}

function arrayToExcel(data) {
    const worksheet = XLSX.utils.aoa_to_sheet(data);
    const workbook = XLSX.utils.book_new();
    XLSX.utils.book_append_sheet(workbook, worksheet, "Sheet1");
    return XLSX.write(workbook, { type: "array", bookType: "xlsx" });
}

function DecryptionEncoding(){
    const decoidngModeDataList = { mode: resultMode, keyData: resultData }
    $.ajax({
        url: "/decodingData",
        type: "POST",
        contentType: "application/json;charset=UTF-8",
        data: JSON.stringify(decoidngModeDataList),
    }).done(function(response) {
        console.log(response);

        // 표를 추가할 컨테이너 요소
        const container = document.querySelector(".card-table");

        const empty = document.createElement("br");
        empty.className = "br";

        const h5 = document.createElement("h5");
        h5.className = "h5";
        h5.textContent = "검증";

        // 마지막 행 제외한 데이터 슬라이스
        const dataToDisplay = response.slice(0, -1);

        // 미리보기 테이블 생성
        const previewTable = createPreviewTable(dataToDisplay);

        // 미리보기 테이블 생성

        container.appendChild(empty);
        container.appendChild(h5);
        container.appendChild(previewTable);

        // 여기에 응답 처리 로직 작성
    }).fail(function(jqXHR, textStatus, errorThrown) {
        console.error("Request failed: " + textStatus + ", Error: " + errorThrown);

        // 여기에 실패 처리 로직 작성
    });
}














