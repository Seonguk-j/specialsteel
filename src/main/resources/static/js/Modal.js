// 날짜 조회 모달창 ======================================================================
// 열기
function showModal() {

    const modal = document.getElementById('myModal');
    modal.style.display = 'block';


}

//닫기
function closeModal() {
    const modal = document.getElementById('myModal');
    modal.style.display = 'none';


}
// 조회 Esc 키로 모달 창 닫기 처리
window.addEventListener("keydown", function (event) {
    if (event.key === "Escape" || event.key === "Esc") {
        closeModal();
    }
});


// 저장 모달창 ======================================================================

function showSaveModal() {
    const modal = document.getElementById('saveModal');
    modal.style.display = 'block';
}

//닫기
function closeSaveModal() {
    const modal = document.getElementById('saveModal');
    modal.style.display = 'none';
}

// 일괄저장 모달창 ======================================================================

function showAllSaveModal() {
    const modal = document.getElementById('allSaveModal');
    modal.style.display = 'block';
}

//닫기
function closeAllSaveModal() {
    const modal = document.getElementById('allSaveModal');
    modal.style.display = 'none';
}


// 모달

// 타이틀 검색
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
// 클릭한 리스트 아이템의 id 값을 저장할 변수를 추가합니다.
let selectedId = null;

function displayData(data) {
    // 리스트 div를 찾습니다.
    const listContainer = document.getElementById("listContainer");
    const paginationContainer = document.getElementById("paginationContainer");

    // 리스트를 초기화합니다.
    listContainer.innerHTML = "";
    paginationContainer.innerHTML = "";



    let selectedIdx = -1; // 현재 선택된 아이템의 인덱스를 추적합니다.

    let currentPage = 1;
    const itemsPerPage = 10;

    // 현재 페이지의 시작과 끝 인덱스를 계산합니다.
    const startIndex = (currentPage - 1) * itemsPerPage;
    const endIndex = Math.min(startIndex + itemsPerPage, data.length);

    // Create the header row with column headings
    const headerRow = document.createElement("div");
    headerRow.classList.add("list-header");

    const numberHeading = document.createElement("div");
    numberHeading.textContent = "번호";
    headerRow.appendChild(numberHeading);

    const dateHeading = document.createElement("div");
    dateHeading.textContent = "날짜";
    headerRow.appendChild(dateHeading);

    const commentHeading = document.createElement("div");
    commentHeading.textContent = "코멘트";
    headerRow.appendChild(commentHeading);

    const comparisonAlgorithmHeading = document.createElement("div");
    comparisonAlgorithmHeading.textContent = "비교알고리즘개수";
    headerRow.appendChild(comparisonAlgorithmHeading);

    listContainer.appendChild(headerRow);

    // 받은 데이터를 이용하여 리스트를 생성합니다.
    //for (let i = 0; i < data.length; i++) {
    for (let i = startIndex; i < endIndex; i++) {
        const listItem = document.createElement("div");
        listItem.classList.add("list-item");

        // Create elements for each column
        const numberCell = document.createElement("div");
        numberCell.textContent = i + 1;
        listItem.appendChild(numberCell);

        const dateCell = document.createElement("div");
        dateCell.textContent = data[i].date;
        listItem.appendChild(dateCell);

        const commentCell = document.createElement("div");
        commentCell.textContent = data[i].comment;
        listItem.appendChild(commentCell);

        const comparisonAlgorithmCell = document.createElement("div");
        comparisonAlgorithmCell.textContent = data[i].amount;
        listItem.appendChild(comparisonAlgorithmCell);

        // 클릭 상태를 저장하는 변수를 추가합니다.
        let isClicked = false;

        // 각 리스트 아이템에 클릭 이벤트 리스너를 추가합니다.
        listItem.addEventListener("click", () => {
            // 이미 선택된 항목이 다시 클릭된 경우, 선택을 해제합니다.
            if (selectedIdx === i) {
                selectedIdx = -1;
                listItem.classList.remove("checked-item");
                const fetchButton = document.getElementById("fetchButton");
                //fetchButton.textContent = "조회";
                fetchButton.setAttribute("onclick", "fetchData()");
            } else {
                // 다른 항목이 이미 선택된 경우, 선택을 해제하고 현재 항목을 선택합니다.
                if (selectedIdx !== -1) {
                    const previousSelectedItems = listContainer.querySelectorAll(".list-item.checked-item");
                    previousSelectedItems.forEach(item => item.classList.remove("checked-item"));
                }
                selectedIdx = i;
                listItem.classList.add("checked-item");
                const fetchButton = document.getElementById("fetchButton");
                //fetchButton.textContent = "조회";
                fetchButton.setAttribute("onclick", "fetchDataById()");
            }

            // 클릭한 항목의 id 값을 저장합니다.
            selectedId = data[i].id;

            // 원하는 동작을 수행합니다. (예를 들어, 더 자세한 정보 표시 또는 다른 페이지로 이동)
            console.log("클릭한 항목:", data[i]);
        });

        // 리스트 아이템을 클릭 가능하도록 CSS 클래스를 추가합니다 (선택사항)
        listItem.classList.add("clickable-list-item");

        listContainer.appendChild(listItem);
    }

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
        if (event.target.classList.contains(".list-item")) {
            // 다른 항목을 클릭했을 때 선택이 해제되도록 처리합니다.
            selectedIdx = -1;
            listContainer.querySelectorAll(".list-item").forEach(item => item.classList.remove("checked-item"));
            const fetchButton = document.getElementById("fetchButton");
            //fetchButton.textContent = "조회";
            fetchButton.setAttribute("onclick", "fetchData()");
        }
    });

}


// 조회 버튼 클릭 시 실행될 함수를 정의합니다.
function fetchDataById() {
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
                showOriFile(data.oriResults);

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
            })
            .catch(error => {
                console.error('Error:', error);
            });
    }
}

function showUploadedList1(revResults) {
    //event.preventDefault()
    var html = "";
    html += "<div class='changeTest'>";
    for (var i = 0; i < revResults.length; i++) {
        html += "<a href='#' class='btn btn-primary m-1' onclick='sendRevFileName1(\"" + i + "\")'>" + (i + 1) + "</a>";
    }
    html += "</div>";

    $("#showResult").html(html);
}



// 날짜 지정
function formatDate(date) {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, "0"); // 월은 0부터 시작하므로 1을 더한 후, 두 자리로 만듭니다.
    const day = String(date.getDate()).padStart(2, "0"); // 일도 두 자리로 만듭니다.
    const hours = String(date.getHours()).padStart(2, "0"); // 시간도 두 자리로 만듭니다.
    const minutes = String(date.getMinutes()).padStart(2, "0"); // 분도 두 자리로 만듭니다.

    return `${year}-${month}-${day} ${hours}:${minutes}`;
}
function excelDownload() {

    window.location.href = '/excel/download/' + selectedId;

}