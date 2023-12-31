/*
    1. 날짜 조회 모달창
    2. 날짜 지정
    3. 일괄저장 모달창
    4. 타이틀 검색
    5. 변환 선택 모달 encoding.html
*/



//  1. 날짜 조회 모달창 ======================================================================

// 모달 열기 함수
function showModal() {
    var modalBackground = document.getElementById('modalBackground');
    var modal = document.getElementById('myModal');
    displayList();
    modalBackground.style.display = 'block';
    modal.style.display = 'block';
}

// 모달 닫기 함수
function closeModal() {
    var modalBackground = document.getElementById('modalBackground');
    var modal = document.getElementById('myModal');
    modalBackground.style.display = 'none';
    modal.style.display = 'none';
}

function displayList() {
    fetch('/api/displayList', {
        method: 'POST',
    })
        .then(response => response.json())
        .then(data => {
            // 받은 데이터를 리스트로 표시합니다.
            displayData(data);
        })
        .catch(error => {
            console.error('Error:', error);
        });
}



// 조회 Esc 키로 모달 창 닫기 처리
window.addEventListener("keydown", function (event) {
    if (event.key === "Escape" || event.key === "Esc") {
        closeModal();
    }
});

// 2. 날짜 지정=====================================================================================
function formatDate(date) {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, "0"); // 월은 0부터 시작하므로 1을 더한 후, 두 자리로 만듭니다.
    const day = String(date.getDate()).padStart(2, "0"); // 일도 두 자리로 만듭니다.
    const hours = String(date.getHours()).padStart(2, "0"); // 시간도 두 자리로 만듭니다.
    const minutes = String(date.getMinutes()).padStart(2, "0"); // 분도 두 자리로 만듭니다.

    return `${year}-${month}-${day} ${hours}:${minutes}`;
}
function excelDownload() {
    var number = "";
    newOrder.forEach(name => {
        if (name.length > 0) {
            number += name[0]; // 각 이름의 첫 번째 글자를 추출하여 배열에 추가
        }
    });
    window.location.href = '/excel/download/' + selectedId  + '/' + number;

}

// 3. 일괄저장 모달창 ======================================================================

function showAllSaveModal() {
    var modalBackground = document.getElementById('modalBackground');
    const modal = document.getElementById('allSaveModal');
    modalBackground.style.display = 'block';
    modal.style.display = 'block';
}

//닫기
function closeAllSaveModal() {
    var modalBackground = document.getElementById('modalBackground');
    const modal = document.getElementById('allSaveModal');
    modalBackground.style.display = 'none';
    modal.style.display = 'none';
}


// 모달====================================

// 4. 타이틀 검색
function titleSearch() {
    const searchInput = document.getElementById("searchWord").value;
    
    // 검색어만 서버로 전송합니다.
    fetch('/api/searchByTitle', {
        method: 'POST',
        body: searchInput,
    })
        .then(response => response.json())
        .then(data => {
            // 받은 데이터를 리스트로 표시합니다.
            displayData(data);
        })
        .catch(error => {
            console.error('Error:', error);
        });
}


// 조회
function fetchData() {

    // 선택한 날짜 정보를 가져옵니다.
    const startDate = document.getElementById("startDate").value;
    const endDate = document.getElementById("endDate").value;

    // 날짜 정보를 서버로 전송합니다.
    fetch('/api/getDataByDate', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            startDate: startDate,
            endDate: endDate,
        }),
    })
        .then(response => response.json())
        .then(data => {
            // 서버에서 받은 데이터를 리스트로 표시합니다.
            console.log(data);
            displayData(data);

        })
        .catch(error => {
            console.error('Error:', error);
        });
}

// 클릭한 리스트 아이템의 id 값을 저장할 변수를 추가합니다.
let selectedId = null;



let currentPage = 1;
const itemsPerPage = 10;

function displayData(data) {
    // 리스트 div를 찾습니다.
    const listContainer = document.getElementById("listContainer");
    const paginationContainer = document.getElementById("paginationContainer");

    // 리스트를 초기화합니다.
    listContainer.innerHTML = "";
    paginationContainer.innerHTML = "";
    let selectedIdx = -1; // 현재 선택된 아이템의 인덱스를 추적합니다.


    // 현재 페이지의 시작과 끝 인덱스를 계산합니다.
    const startIndex = (currentPage - 1) * itemsPerPage;
    const endIndex = Math.min(startIndex + itemsPerPage, data.length);

    // Create the table element
    const table = document.createElement("table");
    table.classList.add("list-table");

    // Create the table header with column headings
    const thead = document.createElement("thead");
    const headerRow = document.createElement("tr");

    const numberHeading = document.createElement("th");
    numberHeading.textContent = "No";
    headerRow.appendChild(numberHeading);

    const titleHeading = document.createElement("th");
    titleHeading.textContent = "제목";
    headerRow.appendChild(titleHeading);

    const dateHeading = document.createElement("th");
    dateHeading.textContent = "날짜";
    headerRow.appendChild(dateHeading);

    const comparisonAlgorithmHeading = document.createElement("th");
    comparisonAlgorithmHeading.textContent = "비교개수";
    headerRow.appendChild(comparisonAlgorithmHeading);

    thead.appendChild(headerRow);
    table.appendChild(thead);

    // Create the table body
    const tbody = document.createElement("tbody");

    for (let i = startIndex; i < endIndex; i++) {
        const listItem = document.createElement("tr");

        // Create elements for each column
        const numberCell = document.createElement("td");
        numberCell.textContent = i + 1;
        numberCell.classList.add("column-number"); // 클래스 추가
        listItem.appendChild(numberCell);

        const titleCell = document.createElement("td");
        titleCell.textContent = data[i].title;
        titleCell.classList.add("column-title"); // 클래스 추가
        listItem.appendChild(titleCell);

        const dateCell = document.createElement("td");
        dateCell.textContent = data[i].date;
        const date = new Date(data[i].date); // 받은 날짜 정보를 JavaScript Date 객체로 변환합니다.
        const formattedDate = formatDate(date); // 원하는 형식으로 날짜를 변환하는 함수를 호출합니다.
        dateCell.textContent = formattedDate;
        dateCell.classList.add("column-date"); // 클래스 추가
        listItem.appendChild(dateCell);

        const comparisonAlgorithmCell = document.createElement("td");
        comparisonAlgorithmCell.textContent = data[i].amount;
        comparisonAlgorithmCell.classList.add("column-comparison"); // 클래스 추가
        listItem.appendChild(comparisonAlgorithmCell);

        // 클릭 상태를 저장하는 변수를 추가합니다.
        let isClicked = false;

        // 각 리스트 아이템에 클릭 이벤트 리스너를 추가합니다.
        listItem.addEventListener("click", () => {
            if (selectedIdx === i) {
                // 이미 선택된 항목을 클릭한 경우, 선택을 해제합니다.
                selectedIdx = -1;
                listItem.classList.remove("checked-item");
                const fetchButton = document.getElementById("fetchButton");
                //fetchButton.textContent = "조회";
                fetchButton.setAttribute("onclick", "fetchData()");
                selectedId = null;
            } else {
                // 다른 항목이 이미 선택된 경우, 선택을 해제하고 현재 항목을 선택합니다.
                if (selectedIdx !== -1) {
                    const previousSelectedItem = listContainer.querySelector(".clickable-list-item.checked-item");
                    previousSelectedItem.classList.remove("checked-item");
                }
                selectedIdx = i;
                listItem.classList.add("checked-item");
                const fetchButton = document.getElementById("fetchButton");
                //fetchButton.textContent = "조회";
                fetchButton.setAttribute("onclick", "fetchDataById()");
                selectedId = data[i].id;
            }

            // 원하는 동작을 수행합니다. (예를 들어, 더 자세한 정보 표시 또는 다른 페이지로 이동)
            console.log("클릭한 항목:", data[i]);
            console.log("선택된 id: " + selectedId);
        });

        // 리스트 아이템을 클릭 가능하도록 CSS 클래스를 추가합니다 (선택사항)
        listItem.classList.add("clickable-list-item");

        tbody.appendChild(listItem);
    }

    table.appendChild(tbody);
    listContainer.appendChild(table);

    // 페이징 컨트롤을 생성합니다.
    const totalPages = Math.ceil(data.length / itemsPerPage);
    for (let pageNum = 1; pageNum <= totalPages; pageNum++) {
        const pageButton = document.createElement("button");
        pageButton.textContent = pageNum;
        pageButton.classList.add("pagination-button");
        if (pageNum === currentPage) {
            pageButton.classList.add("active");
        }

        // 페이지 변경을 처리하는 클릭 이벤트 리스너를 추가합니다.
        pageButton.addEventListener("click", () => {
            currentPage = pageNum;
            displayData(data); // 선택한 페이지에 해당하는 데이터로 리스트를 다시 렌더링합니다.
        });

        paginationContainer.appendChild(pageButton);
    }

    // 전체 리스트 컨테이너에 클릭 이벤트 리스너를 추가합니다.
    listContainer.addEventListener("click", (event) => {
        // 클릭한 대상이 리스트 아이템이 아닌 경우에만 처리합니다.
        if (event.target.classList.contains("clickable-list-item")) {
            // 다른 항목을 클릭했을 때 선택이 해제되도록 처리합니다.
            selectedIdx = -1;
            listContainer.querySelectorAll(".list-item").forEach(item => item.classList.remove("checked-item"));
            const fetchButton = document.getElementById("fetchButton");
            //fetchButton.textContent = "조회";
            fetchButton.setAttribute("onclick", "fetchData()");
            selectedId = null;
        }
    });
}


// 조회 버튼 클릭 시 실행될 함수를 정의합니다.
function fetchDataById() {
    showRevInit();
    showOriInit();
    chartInit();
    tableInit();

    const fetchButton = document.getElementById("fetchButton");
    fetchButton.setAttribute("onclick", "fetchData()");
    listContainer.innerHTML = "";
    paginationContainer.innerHTML = "";

    // 검색어 초기화
    document.getElementById("searchWord").value = "";

    // 날짜 초기화
    document.getElementById("startDate").value = "";
    document.getElementById("endDate").value = "";
    closeModal();    // 선택한 id 값으로 서버로 데이터를 요청합니다.
    if (selectedId !== null) {
        fetch(`/api/getDataById/${selectedId}`)
            .then(response => response.json())
            .then(data => {
                // 서버에서 받은 데이터를 원하는 방식으로 화면에 표시합니다.

                console.log(data);
                // showOriFile(data.oriResults);

                var html = "";

                html += "<div class='insert list-group'>"
                html += "<li class='list-group-item'>합금철 총 투입비용 : </li>";
                html += "<li class='list-group-item'>합금철 총 투입량 : </li>";
                html += "<li class='list-group-item'>예상 용강량 :</li>";
                html += "<li class='list-group-item'>방법 : </li>";
                html += "<div id='uploadResult'></div>";
                html += "<div id='showResult'></div>";
                html += "<div id='showResult1'></div>";
                html += "</div>";

                $(".insert").html(html);

                showUploadedList1(data.revResults);
                discardSave();
            })
            .catch(error => {
                console.error('Error:', error);
            });
    }
}

function discardSave() {
    var html = "";

    $("#save-card").html(html);
}

function showUploadedList1(revResults) {
    //event.preventDefault()
    var html = "";
    html += "<div class='changeTest'>";
    for (var i = 0; i < revResults.length; i++) {
        html += "<a href='#' id='btn_" + i + "' class='pagebtn btn m-1' onclick='sendRevFileName1(\"" + i + "\")'>" + (i + 1) + "</a>";
    }
    html += "</div>";

    $("#showResult").html(html);

}

// =========================================================================================
// 5. 변환 선택 모달 encoding.html ==========================

function closeChoiceModal() {
    const modal = document.getElementById('choiceModal');
    modal.style.display = 'none';
}


//변환 파일 선택 모달 ==========================
function choiceFileModal() {

    //미리보기 영역 초기화
    const previewArea = document.getElementById('previewArea');
    previewArea.innerHTML = '<h5 class="h5">미리보기</h5><div class="card-table"></div>';

    var modalBackground = document.getElementById('modalBackground');
    const modal = document.getElementById('choiceFileModal');
    modalBackground.style.display = 'block';
    modal.style.display = 'block';
}


function closeChoiceFileModal() {
    var modalBackground = document.getElementById('modalBackground');
    const modal = document.getElementById('choiceFileModal');
    modalBackground.style.display = 'none';
    modal.style.display = 'none';

    // 파일 입력 초기화
    const changeFileInput = document.getElementById('changeFileInput');
    changeFileInput.value = '';

    // 라디오 버튼 초기화
    const standardizationRadio = document.getElementById('standardizationRadio');
    standardizationRadio.checked = true;
    const normalizationRadio = document.getElementById('normalizationRadio');
    normalizationRadio.checked = false;
}






//엑셀 저장 미리 보기 모달 관련 js ==========================================================================
function excelView() {
    if(selectedId != null) {
        const modal = document.getElementById('excelSaveModal');
        modal.style.display = 'block';
    } else {
        alert("데이터를 선택 해주세요.");
    }
}

//닫기
function closeExcelView() {
    const modal = document.getElementById('excelSaveModal');
    modal.style.display = 'none';
}

function showIpModal() {
    const modal = document.getElementById('showIpModal');
    modal.style.display = 'block';
}

//Ip modal
function closeIpModal() {
    const modal = document.getElementById('showIpModal');
    location.reload();
    modal.style.display = 'none';
}
//IP 삭제
function deleteIp(id){
    $.ajax({
        url: '/deleteIp/'+id,
        method: 'POST',
        success: function (data) {

            var html = "<table class='list-table'>";
            html += "<thead>";
            html += "<th>No</th>";
            html += "<th>Ip 주소</th>";
            html += "<th></th>";
            html += "</thead>";
            html += "<tbody>";

    // ipDTOList를 이용하여 각 아이템 반복 출력
            data.forEach(function(ipAddress, index) {
                html += "<tr>";
                html += "<td>" + (index + 1) + "</td>";
                html += "<td>" + ipAddress.address + "</td>";
                html += "<td>";
                html += "<button class='Closebtn' id='delete-btn' data-id='" + ipAddress.id + "' onclick='deleteIp(" + ipAddress.id + ")'>X</button>";
                html += "</td>";
                html += "</tr>";
            });

            html += "</tbody>";
            html += "</table>";

    // 생성한 HTML을 .allchart 요소에 추가
            $(".list-table").html(html);
        },
        error: function () {

        }
    });
}
function promptOn(){
    var ipAddress = prompt("ip 주소를 입력하세요.", "기본 값");

    if (ipAddress !== null) {
        $.ajax({
            url: '/add_ip',
            method: 'POST',
            data: {ipAddress: ipAddress},
            success: function (data) {

                var html = "<table class='list-table'>";
                html += "<thead>";
                html += "<th>No</th>";
                html += "<th>Ip 주소</th>";
                html += "<th></th>";
                html += "</thead>";
                html += "<tbody>";

                // ipDTOList를 이용하여 각 아이템 반복 출력
                data.forEach(function (ipAddress, index) {
                    html += "<tr>";
                    html += "<td>" + (index + 1) + "</td>";
                    html += "<td>" + ipAddress.address + "</td>";
                    html += "<td>";
                    html += "<button class='Closebtn' id='delete-btn' data-id='" + ipAddress.id + "' onclick='deleteIp(" + ipAddress.id + ")'>X</button>";
                    html += "</td>";
                    html += "</tr>";
                });

                html += "</tbody>";
                html += "</table>";

                // 생성한 HTML을 .allchart 요소에 추가
                $(".list-table").html(html);
            },
            error: function () {

            }
        });
    }
}