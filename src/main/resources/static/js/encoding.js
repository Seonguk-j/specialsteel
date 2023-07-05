// 합금철 투입량 표 데이터
var alloyData = [
    ["합금철 종류", "AAA", "BBB", "CCC", "DDD", "EEE", "FFF", "..."],
    ["기존 알고리즘", "-", "-", "-", "-", "-", "-", "..."],
    ["수정 알고리즘", "-", "-", "-", "-", "-", "-", "..."]
]

// 합금철 투입량 표 생성 함수
function createAlloyTable(data) {
    var table = document.createElement("table");
    var caption = document.createElement("caption");
    caption.classList.add("title");

    table.appendChild(caption);

    for (var i = 0; i < data.length; i++) {
        var row = document.createElement("tr");

        for (var j = 0; j < data[i].length; j++) {
            var cell = document.createElement(i === 0 ? "th" : "td");
            cell.textContent = data[i][j];
            row.appendChild(cell);
        }

        table.appendChild(row);
    }

    return table;
}


// 표를 추가할 컨테이너 요소
var container = document.querySelector(".card-table");

// 합금철 투입량 표 생성
var alloyTable = createAlloyTable(alloyData);
var alloyTableContainer = document.createElement("div");
alloyTableContainer.classList.add("table-responsive");
alloyTableContainer.appendChild(alloyTable);

// 제목 추가
var alloyTitle = document.createElement("div");
alloyTitle.classList.add("title");
alloyTitle.textContent = "합금철별 투입량 :";
container.appendChild(alloyTitle);
container.appendChild(alloyTableContainer);


// 컨테이너에 표 추가
container.appendChild(resultTableContainer);