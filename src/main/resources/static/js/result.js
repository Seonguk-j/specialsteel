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

// 합금철 투입량 표 생성 함수
function createAlloyTable(response) {
    var table = document.createElement("table");
    var caption = document.createElement("caption");
    caption.classList.add("title");

        table.appendChild(caption);

    var ori = response.oriAlloyInputs;
    var rev = response.revAlloyInputs;

    var row1 = document.createElement("tr");
    var row2 = document.createElement("tr");
    var row3 = document.createElement("tr");

    head = document.createElement("th");
    head.textContent = "합금철 종류";
    cell1 = document.createElement("td");
    cell1.textContent = "기존 알고리즘";
    cell2 = document.createElement("td");
    cell2.textContent = "수정 알고리즘";

    row1.appendChild(head);
    row2.appendChild(cell1);
    row3.appendChild(cell2);


    var head, cell1,cell2;
    for(var key in ori) {
        head = document.createElement("th");
        head.textContent = key;
        cell1 = document.createElement("td");
        cell1.textContent = ori[key];
        cell2 = document.createElement("td");
        cell2.textContent = rev[key];
        row1.appendChild(head);
        row2.appendChild(cell1);
        row3.appendChild(cell2);
    }

    table.appendChild(row1);
    table.appendChild(row2);
    table.appendChild(row3);

    return table;
}

// result 예상 성분 표 생성 함수
function createMaterialTable(response) {
    var table = document.createElement("table");
    var caption = document.createElement("caption");
    caption.classList.add("title");

        table.appendChild(caption);

    var ori = response.oriMaterials;
    var rev = response.revMaterials;

    var row1 = document.createElement("tr");
    var row2 = document.createElement("tr");
    var row3 = document.createElement("tr");

    head = document.createElement("th");
    head.textContent = "예상 성분";
    cell1 = document.createElement("td");
    cell1.textContent = "기존 알고리즘";
    cell2 = document.createElement("td");
    cell2.textContent = "수정 알고리즘";

    row1.appendChild(head);
    row2.appendChild(cell1);
    row3.appendChild(cell2);


    var head, cell1,cell2;
    for(var key in ori) {
        head = document.createElement("th");
        head.textContent = key;
        cell1 = document.createElement("td");
        cell1.textContent = ori[key];
        cell2 = document.createElement("td");
        cell2.textContent = rev[key];
        row1.appendChild(head);
        row2.appendChild(cell1);
        row3.appendChild(cell2);
    }

    table.appendChild(row1);
    table.appendChild(row2);
    table.appendChild(row3);

    return table;
}

function drawAlloyTable(response) {
    // 표를 추가할 컨테이너 요소
    var container = document.querySelector(".alloy-table");

    // 이전 테이블을 제거합니다.
    while (container.firstChild) {
        container.firstChild.remove();
    }

    // 합금철 투입량 표 생성
    var alloyTable = createAlloyTable(response);
    var alloyTableContainer = document.createElement("div");
    alloyTableContainer.className = "alloy-table";
    alloyTableContainer.appendChild(alloyTable);
    var alloyTitle = document.createElement("div");
    alloyTitle.classList.add("title");
    alloyTitle.textContent = "합금철별 투입량 :";
    container.appendChild(alloyTitle);
    container.appendChild(alloyTableContainer);

}

function drawMaterialTable(response) {
    // 표를 추가할 컨테이너 요소
    var container = document.querySelector(".material-table");

    // 이전 테이블을 제거합니다.
    while (container.firstChild) {
        container.firstChild.remove();
    }

    // 합금철 투입량 표 생성
    var materialTable = createMaterialTable(response);
    var materialTableContainer = document.createElement("div");
    materialTableContainer.className = "material-table";
    materialTableContainer.appendChild(materialTable);
    var materialTitle = document.createElement("div");
    materialTitle.classList.add("title");
    materialTitle.textContent = "result 예상 성분 :";
    container.appendChild(materialTitle);
    container.appendChild(materialTableContainer);
}

// 합금철별 투입량 차트
google.charts.load('current', {'packages':['bar']});
google.charts.setOnLoadCallback();

function drawAlloyChart(response) {
    var data = new google.visualization.DataTable();
    // console.log(JSON.stringify(response));
    data.addColumn('string', 'Ingredient');
    data.addColumn('number', 'Diff');

    for(var key in response) {
        var value = response[key];
        data.addRow([key, value]);
    }

    var options = {
        chart: {
            title: '합금철별 투입량',
            subtitle: '',
        }
    };

    var chart = new google.charts.Bar(document.getElementById('alloy_chart'));

    chart.draw(data, google.charts.Bar.convertOptions(options));
}

// 예상 성분 차트
google.charts.load('current', {'packages':['bar']});
google.charts.setOnLoadCallback();
function drawMaterialChart(response) {
    var data = new google.visualization.DataTable();
    // console.log(JSON.stringify(response));
    data.addColumn('string', 'Ingredient');
    data.addColumn('number', 'Diff');

    // var output = response.diffAlloyInputList;
    // console.log("차트 테스트: " + output);
    for(var key in response) {
        var value = response[key];
        data.addRow([key, value]);
    }

    var options = {
        chart: {
            title: 'result 예상 성분',
            subtitle: '',
        }
    };

    var chart = new google.charts.Bar(document.getElementById('material_chart'));

    chart.draw(data, google.charts.Bar.convertOptions(options));
}

