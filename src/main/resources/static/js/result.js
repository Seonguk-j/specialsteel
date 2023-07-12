// 페이징
// const pagination = document.getElementById('pagination');
//
// // 데이터가 늘어날 때마다 페이지 버튼을 동적으로 추가하는 함수
// function addPageButton(pageNumber) {
//     const li = document.createElement('li');
//     li.classList.add('page-item');
//     const a = document.createElement('a');
//     a.classList.add('page-link');
//     a.href = '#';
//     a.textContent = pageNumber.toString();
//     li.appendChild(a);
//     pagination.appendChild(li);
// }
//
// // 예시 5개
// for (let i = 2; i <= 5; i++) {
//     addPageButton(i);
// }
//
// function compare() {
//     var originalFile = document.getElementById('originalFile1').files[0];
//     var modifiedFiles = document.getElementById('originalFile2').files;
//
//     var formData = new FormData();
//     formData.append('originalFile', originalFile);
//
//     for (var i = 0; i < modifiedFiles.length; i++) {
//         formData.append('modifiedFiles', modifiedFiles[i]);
//     }
//
//     var xhr = new XMLHttpRequest();
//     xhr.open('POST', '/compare', true);
//     xhr.onreadystatechange = function() {
//         if (xhr.readyState === 4 && xhr.status === 200) {
//             var response = JSON.parse(xhr.responseText);
//             displayResultButtons(response.modifiedFiles);
//         }
//     };
//     xhr.send(formData);
// }
//
// function displayResultButtons(modifiedFiles) {
//     var resultButtonsDiv = document.getElementById('resultButtons');
//     resultButtonsDiv.innerHTML = '';
//
//     for (var i = 0; i < modifiedFiles.length; i++) {
//         var button = document.createElement('button');
//         button.textContent = modifiedFiles[i];
//         resultButtonsDiv.appendChild(button);
//     }
// }
//
//
//







// 표
// 합금철 투입량 표 데이터
var alloyData = [
    ["합금철 종류", "철냉각제", "Cr(H)60% 하이크롬 60%", "Cr(H)57% 차지크롬 57%", "Cr(H)57% 차지크롬 47%", "Cr(H)57% 차지크롬 47% Fine", "Cr(L)60% 로크롬", "..."],
    ["기존 알고리즘", "-", "-", "-", "-", "-", "-", "..."],
    ["수정 알고리즘", "-", "-", "-", "-", "-", "-", "..."]
];

// result 예상 성분 표 데이터
var resultData = [
    ["성분 종류", "C", "Si", "Mn", "P", "S", "Ni", "..."],
    ["기존 알고리즘", "-", "-", "-", "-", "-", "-", "..."],
    ["수정 알고리즘", "-", "-", "-", "-", "-", "-", "..."]
];

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

// result 예상 성분 표 생성 함수
function createResultTable(data) {
    var table = document.createElement("table");
    var caption = document.createElement("caption");
    caption.classList.add("title");

    table.appendChild(caption);

    for (var i = 0; i < data.length; i++) {
        var row = document.createElement("tr");

        for (var j = 0; j < data[i].length; j++) {
            var cell = document.createElement("td");
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
alloyTableContainer.appendChild(alloyTable);

// result 예상 성분 표 생성
var resultTable = createResultTable(resultData);
var resultTableContainer = document.createElement("div");
resultTableContainer.appendChild(resultTable);

// 제목 추가
var alloyTitle = document.createElement("div");
alloyTitle.classList.add("title");
alloyTitle.textContent = "합금철별 투입량 :";
container.appendChild(alloyTitle);
container.appendChild(alloyTableContainer);

var resultTitle = document.createElement("div");
resultTitle.classList.add("title");
resultTitle.textContent = "result 예상 성분 :";
container.appendChild(resultTitle);
container.appendChild(resultTableContainer);






//차트

google.charts.load('current', {'packages':['bar']});
google.charts.setOnLoadCallback(drawChart1);

function drawChart1() {
    var data = google.visualization.arrayToDataTable([
        ['Year', 'Sales', 'Expenses'],
        ['철냉각제', 500, 400],
        ['Fe-Ni(35%) 페로니켈 35%', 2000, 1800],
        ['Me-Ni(99%) 메탈니켈', 2500, 3000]
    ]);

    var options = {
        chart: {
            title: '합금철별 투입량',
            subtitle: '',
        }
    };

    var chart = new google.charts.Bar(document.getElementById('columnchart_material'));

    chart.draw(data, google.charts.Bar.convertOptions(options));
}

/* 차트 2 */
google.charts.load('current', {'packages':['bar']});
google.charts.setOnLoadCallback(drawChart2);

function drawChart2() {
    var data = google.visualization.arrayToDataTable([
        ['Year', 'Sales', 'Expenses'],
        ['C', 100, 400],
        ['Si', 1170, 460],
        ['Mn', 660, 1120],
        ['P', 1170, 460],
        ['S', 1170, 460],
        ['Ni', 1170, 460],
        ['Cr', 1170, 460],
        ['Mo', 1170, 460],
        ['Cu', 1170, 460],
        ['Sn', 1170, 460],
        ['Tol-Al', 1170, 460],
    ]);

    var options = {
        chart: {
            title: 'result 예상 성분',
            subtitle: '',
        }
    };

    var chart = new google.charts.Bar(document.getElementById('columnchart_material2'));

    chart.draw(data, google.charts.Bar.convertOptions(options));
}



