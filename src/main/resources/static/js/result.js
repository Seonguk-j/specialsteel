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




//
// // 파일 선택 text 지우기 ======================================================================
// $(document).ready(function() {
//     $(".uploadBtn").click(function() {
//         // 파일 선택 텍스트를 지우기 위해 파일 선택 요소의 값을 초기화합니다.
//         $(this).prev("input[type='file']").val("");
//     });
// });
//
//
//
// // 파일 업로드 ======================================================================
//
// $('.uploadBtn').click(function (){
//     // event.preventDefault();
//     var formData = new FormData();
//     var inputFile = $("input[type='file']");
//     var files = inputFile[1].files;
//
//     // 파일업로드 갯수 제한
//     var maxFileCount = 10; // 최대 업로드 갯수
//     if (files.length > maxFileCount) {
//         alert("파일 업로드는 최대 " + maxFileCount + "개까지 가능합니다.");
//         return;
//     }
//
//     for(var i=0; i<files.length; i++){
//         console.log(files[i]);
//         formData.append("uploadfiles", files[i]);
//     }
//
//     //uplaod ajax
//     $.ajax({
//         url: '/uploadAjax',
//         processData: false,
//         contentType: false,
//         data: formData,
//         type: 'POST',
//         dataType: 'json',
//         success: function (result){
//             console.log(result);
//             showUploadedList(result);
//         },
//         error:function (jqXHR, textStatus, errorThorwn){
//             console.log(textStatus);
//         }
//     });
//     function showUploadedList(arr) {
//         event.preventDefault();
//         console.log(arr[0]);
//         var html = "";
//
//         html += "<div>";
//             for (var i = 0; i < arr.length; i++) {
//                 html += "<a href='#' class='btn btn-primary m-1'  onclick='sendFileName(\"" + i + "\")'>" + (i + 1) + "</a>";
//             }
//         html += "</div>";
//         $(".uploadResult").html(html);
//
//     }
//
//
// });
// function sendFileName(fileName) {
//     // Ajax 요청을 사용하여 백엔드에 데이터를 전송
//     $.ajax({
//         url: '/sendFileName',
//         method: 'POST',
//         data: { fileName: fileName },
//         success: function(response) {
//             // 요청이 성공한 경우의 동작
//             console.log(response);
//             showFileList(response);
//
//         },
//         error: function(error) {
//             // 요청이 실패한 경우의 동작
//             console.log(error);
//         }
//     });
// }
// function showFileList(response) {
//     var html = "";
//     html += "<div class='insert'>"
//     html += "<li>합금철 총 투입비용 : " + response.totalCost + "</li>";
//     html += "<li>합금철 총 투입량 : " + response.totalAmount + "</li>";
//     html += "<li>예상 용강량 : " + response.expectOutput + "</li>";
//     html += "<li>방법 : " + response.method + "</li>";
//     html += "</div>";
//
//
//     $(".insert").html(html);
// }

// 커멘트


//차트 ==================================================================================

// 차트1
    google.charts.load('current', {'packages': ['bar']});
    google.charts.setOnLoadCallback(drawStuff1);

    function drawStuff1() {
        var data = new google.visualization.arrayToDataTable([
            ['Move', 'Percentage'],
            ['철냉각제', 1500],
            ['Fe-Ni(35%) 페로니켈 35%', 800],
            ['Me-Ni(99%) 메탈니켈', 500]
        ]);

        var options = {
            width: 800,
            legend: {position: 'none'},
            chart: {
                title: '합금철별 투입량',
                subtitle: ''
            },
            axes: {
                x: {
                    0: {side: 'top', label: ''} // Top x-axis.
                }
            },
            bar: {groupWidth: "90%"}
        };

        var chart = new google.charts.Bar(document.getElementById('top_x_div1'));
        // Convert the Classic options to Material options.
        chart.draw(data, google.charts.Bar.convertOptions(options));
    };

//차트2
    google.charts.load('current', {'packages': ['bar']});
    google.charts.setOnLoadCallback(drawStuff2);

    function drawStuff2() {
        var data = new google.visualization.arrayToDataTable([
            ['Move', 'Percentage'],
            ['C', -100],
            ['Si', 150],
            ['Mn', 660],
            ['P', 110],
            ['S', -130],
            ['Ni', 170],
            ['Cr', 120],
            ['Mo', 370],
            ['Cu', 270],
            ['Sn', 70],
            ['Tol-Al', 30],
        ]);

        var options = {
            width: 800,
            legend: {position: 'none'},
            chart: {
                title: 'result 예상 성분',
                subtitle: ''
            },
            axes: {
                x: {
                    0: {side: 'top', label: ''} // Top x-axis.
                }
            },
            bar: {groupWidth: "90%"}
        };

        var chart = new google.charts.Bar(document.getElementById('top_x_div2'));
        // Convert the Classic options to Material options.
        chart.draw(data, google.charts.Bar.convertOptions(options));
    };


// 표 =============================================================================
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
