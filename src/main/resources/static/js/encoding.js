// 합금철 투입량 표 데이터
var alloyData = [
    ["합금철 종류", "AAA", "BBB", "CCC", "DDD", "EEE", "FFF", "..."],
    ["기존 알고리즘", "-", "-", "-", "-", "-", "-", "..."],
    ["수정 알고리즘", "-", "-", "-", "-", "-", "-", "..."]
]


let resultData;




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

    console.log(files);
    for (let i = 0; i < files.length; i++) {
        const fileType = files[i].type;
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
            const data2 = data1.pop();
            const mode = result.mode;
            const decodingKey = data[7];

            decodingKeyList = decodingKey;
            let dataArray = [];

            // JSON 데이터를 2차원 배열로 변환
            for (let i = 0; i < data1.length; i++) {
                dataArray[i] = data1[i].map(row => Object.values(row));
            }

            // 표를 추가할 컨테이너 요소
            const container = document.querySelector(".card-table");
            const perPage = 1; // 한 페이지에 보여줄 테이블 개수
            let currentPage = 1;

            function showTable() {
                container.innerHTML = '';
                const startIndex = (currentPage - 1) * perPage;
                const endIndex = startIndex + perPage;

                for (let i = startIndex; i < endIndex && i < dataArray.length; i++) {
                    const previewTable = createPreviewTable(dataArray[i], "합금철별 투입량 :");
                    container.appendChild(previewTable);
                }
            }

            // 표를 추가할 컨테이너 요소
            const wrapper = document.createElement('div');
            wrapper.classList.add('card-table-wrapper');
            const container1 = document.createElement('div');
            container1.classList.add('card-table');
            wrapper.appendChild(container1);

            function showTable() {
                container1.innerHTML = '';
                const startIndex = (currentPage - 1) * perPage;
                const endIndex = startIndex + perPage;

                for (let i = startIndex; i < endIndex && i < dataArray.length; i++) {
                    const previewTable = createPreviewTable(dataArray[i], "합금철별 투입량 :");
                    container1.appendChild(previewTable);
                }
            }

            showTable();

            // 페이징 버튼 생성
            const pagination = document.createElement('div');
            pagination.classList.add('pagination');
            wrapper.appendChild(pagination);

            for (let i = 1; i <= Math.ceil(dataArray.length / perPage); i++) {
                const pageLink = document.createElement('button');
                pageLink.innerText = i;
                pageLink.classList.add('page-link');
                pageLink.addEventListener('click', () => {
                    currentPage = i;
                    showTable();
                });

                pagination.appendChild(pageLink);
            }

            container.appendChild(wrapper);




            console.log("data - "+data);
            console.log(mode);
            console.log(dataArray);
            console.log(data1[0]);
            console.log(data1[1]);
            console.log(data1[2]);
            console.log(data1[3]);
            console.log(data1[4]);
            console.log(data1[5]);
            console.log(data2);
            console.log(data);
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

    // 파일 이름 설정
    const fileName = dateStr + resultData[2][0];
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














