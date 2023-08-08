
/*
    1. 기존알고리즘 결과, 수정알고리즘 결과
    2. 삭제, 파일저장, 일괄저장
    3. 정렬
    4. 표
    5. 차트
 */

//1. 기존알고리즘 결과, 수정알고리즘 결과 ================================================================
var nowIndex;
var nowRevComment;
var nowOriComment;
var compareDTO;
var diffAlloyInputsMap;
var diffMaterialsMap;

$('#oriUploadBtn').click(function (){
    // event.preventDefault();
    var formData = new FormData();
    var inputFile = $("input[type='file']");
    var files = inputFile[1].files;
    formData.append("uploadfiles", files[0]);

    //uplaod ajax
    $.ajax({
        url: '/oriUploadAjax',
        processData: false,
        contentType: false,
        data: formData,
        type: 'POST',
        dataType: 'json',
        success: function (result){
            sendOriFileName(result[0]);
        },
        error:function (){
        }
    });
    $(this).prev("input[type='file']").val("");
});

//request json파일 api 전송



// function sendOriFileName() {
//     // Ajax 요청을 사용하여 백엔드에 데이터를 전송
//     $.ajax({
//         url: '/sendOriFileName',
//         method: 'POST',
//         success: function(response) {
//             // 요청이 성공한 경우의 동작
//             showOriFile(response);
//         },
//         error: function(error) {
//             // 요청이 실패한 경우의 동작
//             console.log(error);
//         }
//     });
// }


function sendOriFileName(index) {

    // Ajax 요청을 사용하여 백엔드에 데이터를 전송
    $.ajax({
        url: '/sendOriFileName',
        data: { index: index },
        method: 'POST',
        success: function(response) {
            // 요청이 성공한 경우의 동작
            showOriFile(response);
        },
        error: function(error) {
            // 요청이 실패한 경우의 동작
            console.log(error);
        }
    });
}


//
function showOriFile(response) {
    var html = "";
    html += "<div class='oriInsert list-group'>"
    html += "<li class='list-group-item'>합금철 총 투입비용 : " + response.totalCost + "</li>";
    html += "<li class='list-group-item'>합금철 총 투입량 : " + response.totalAmount + "</li>";
    html += "<li class='list-group-item'>예상 용강량 : " + response.expectOutput + "</li>";
    html += "<li class='list-group-item'>방법 : " + response.method + "</li>";
    html += "</div>";

    $(".oriInsert").html(html);
    // nowOriComment = $("#commentOriTextarea").val();
}

//업로드 버튼을 누르면 파일을 MultipartFile[]에 담아 백으로 전달
$('#revUploadBtn').click(function (){
    // event.preventDefault();
    var formData = new FormData();

    var inputFile = $("input[type='file']");

    var files = inputFile[1].files;

    for(var i=0; i<files.length; i++){

        formData.append("uploadfiles", files[i]);
    }
    //uplaod ajax
    $.ajax({
        url: '/revUploadAjax',
        processData: false,
        contentType: false,
        data: formData,
        type: 'POST',
        dataType: 'json',
        success: function (result){
            showInit();
            showUploadedList(result);
        },
        error:function (jqXHR, textStatus, errorThrown){
            console.log(textStatus);
        }
    });
    $(this).prev("input[type='file']").val("");
});

//전달 받은 파일의 개수 만큼 버튼 생성, 버튼을 누르면 해당 인덱스를 가지는 json파일 출력
function showUploadedList(arr) {
    event.preventDefault();
    var html = "";
    html += "<div class='changeTest'>";
    for (var i = 0; i < arr; i++) {
        html += "<a href='#' class='pagebtn btn m-1' onclick='sendFileName(\"" + i + "\")'>" + (i+1) + "</a>";
    }
    html += "</div>";
    $("#uploadResult").html(html);

}

function sendFileName(index) {
    sendOriFileName(index);
    sendRevFileName(index);
}
function sendRevFileName(index) {

    $.ajax({
        url: '/sendRevFileName',
        method: 'POST',
        data: { index: index },
        success: function(response) {
            // 요청이 성공한 경우의 동작
            showRevFile(response.revResultDTO);
            showSaveBtn();
            compareDTO = response.compareDTO;
            console.log(compareDTO);
            var html ="";
            html += "<h5 id='chart-title'>요약 지표</h5>";
            html += "<div class='sort-group'>";
            html += "<select class='sort-list' id='sort-select' style='margin-right: 10px' onchange='sendSortData()'>";
            html += "<option>정렬기준</option>";
            html += "<option>이름</option>";
            html += "<option>차이값</option>";
            html += "</select>";
            html += "<div>";
            html += "<input type='radio' id='asc' name='order' value='asc' checked onchange='sendSortData()'>오름차순";
            html += "<input type='radio' id='desc' name='order' value='desc' onchange='sendSortData()'>내림차순";
            html += "</div>";
            html += "</div>";
            $(".allchart").html(html);

            if(Object.keys(response.compareDTO.diffAlloyInputs).length === 0){
                alert("합금철별 투입량에 차이가 없습니다.");

                var html ="";
                html += "<div id='alloy_chart'>";
                html += "<div class='message'>데이터가 없습니다.</div>";
                html += "</div>";

                $("#alloy_chart").html(html);

                html = "<div class='alloy-table'></div>"
                $(".alloy-table").html(html);
            }
            else {
                var order = [];
                var i = 0;
                for(var key in response.compareDTO.diffAlloyInputs){
                    order[i] = key;
                    i++;
                }
                drawAlloyChart(response.compareDTO.diffAlloyInputs, order);
                drawAlloyTable(response.compareDTO, order);
                diffAlloyInputsMap = response.compareDTO.diffAlloyInputs;
            }
            if(Object.keys(response.compareDTO.diffMaterials).length === 0){
                alert("예상 성분에 차이가 없습니다.");

                var html ="";
                html += "<div id='material_chart'>";
                html += "<div class='message'>데이터가 없습니다.</div>";
                html += "</div>";

                $("#material_chart").html(html);

                html = "<div class='material-table'></div>"
                $(".material-table").html(html);
            }else {
                var order = [];
                var i = 0;
                for(var key in response.compareDTO.diffMaterials){
                    order[i] = key;
                    i++;
                }
                drawMaterialChart(response.compareDTO.diffMaterials, order);
                drawMaterialTable(response.compareDTO, order);
                diffMaterialsMap = response.compareDTO.diffMaterials;
            }
        },
        error: function(error) {
            console.log(error);
        }
    });
}

function showRevFile(response) {
    // nowIndex = response.index;
    var html = "";
    html += "<div class='insert list-group'>";
    html += "<li class='list-group-item'>합금철 총 투입비용 : " + response.totalCost + "</li>";
    html += "<li class='list-group-item'>합금철 총 투입량 : " + response.totalAmount + "</li>";
    html += "<li class='list-group-item'>예상 용강량 : " + response.expectOutput + "</li>";
    html += "<li class='list-group-item'>방법 : " + response.method + "</li>";
    html += "<div>";


    //------------------------------ 수정중인부분
    for(var i = 0; i < response.length; i++) {
        html += "<a href='#' id='uploadResult' class='pagebtn btn m-1' onclick='sendFileName(\"" + i + "\")'>" + (i + 1) + "</a>";
    }

    // for (var i = 0; i < response.length; i++) {
    //     if (i === nowIndex) {
    //         html += "<a href='#' id='uploadResult' class='pagebtn btn active m-1' onclick='saveComment(\"" + i + "\",\"" + response.index + "\", $(\"#commentRevTextarea\").val())'>" + (i + 1) + "</a>";
    //     } else {
    //         html += "<a href='#' id='uploadResult' class='pagebtn btn m-1' onclick='saveComment(\"" + i + "\",\"" + response.index + "\", $(\"#commentRevTextarea\").val())'>" + (i + 1) + "</a>";
        // }
    // }

    html += "</div>";
    html += "<div class='delete-group'>";
    html += "<button class='deleteBtn ' data-index='" + response.index + "'> 삭제</button>";
    html += "</div>";
    html += "</div>";


    $(".insert").html(html);
    // nowRevComment = $("#commentRevTextarea").val();
}
function showSaveBtn(){
    var html = "";
    html += "<div class='row'>";
    html += "<div class='card col-12'>";
    html += "<div class='card-body'>";
    html += "<div class='result-save'>";
    // html += "<button class='saveBtn'>저장</button>";
    html += "<button class='allSaveBtn'>일괄 저장</button>";
    html += "</div>";
    html += "</div>";
    html += "</div>";
    html += "</div>";

    $(".hidden-group").html(html);

}


// function saveComment(index, saveIndex, comment) {
//     $.ajax({
//         url: '/saveComment',
//         method: 'POST',
//         data: { index: index,
//             saveIndex: saveIndex,
//             comment: comment},
//         success: function(response) {
//             // 요청이 성공한 경우의 동작
//             showRevFile(response.revResultDTO);
//             showSaveBtn();
//             compareDTO = response.compareDTO;
//
//             var html ="";
//             html += "<h5 id='chart-title'>요약 지표</h5>";
//             html += "<div class='sort-group'>";
//             html += "<select class='sort-list' id='sort-select' style='margin-right: 10px' onchange='sendSortData()'>";
//             html += "<option>정렬기준</option>";
//             html += "<option>이름</option>";
//             html += "<option>차이값</option>";
//             html += "</select>";
//             html += "<div>";
//             html += "<input type='radio' id='asc' name='order' value='asc' checked onchange='sendSortData()'>오름차순";
//             html += "<input type='radio' id='desc' name='order' value='desc' onchange='sendSortData()'>내림차순";
//             html += "</div>";
//             html += "</div>";
//             $(".allchart").html(html);
//
//             if(Object.keys(response.compareDTO.diffAlloyInputs).length === 0){
//                 alert("합금철별 투입량에 차이가 없습니다.");
//
//                 var html ="";
//                 html += "<div id='alloy_chart'>";
//                 html += "<div class='message'>데이터가 없습니다.</div>";
//                 html += "</div>";
//
//
//                 $("#alloy_chart").html(html);
//
//                 html = "<div class='alloy-table'></div>"
//                 $(".alloy-table").html(html);
//             }
//             else {
//                 var order = [];
//                 var i = 0;
//                 for(var key in response.compareDTO.diffAlloyInputs){
//                     order[i] = key;
//                     i++;
//                 }
//                 drawAlloyChart(response.compareDTO.diffAlloyInputs, order);
//                 drawAlloyTable(response.compareDTO, order);
//                 diffAlloyInputsMap = response.compareDTO.diffAlloyInputs;
//             }
//             if(Object.keys(response.compareDTO.diffMaterials).length === 0){
//                 alert("예상 성분에 차이가 없습니다.");
//
//                 var html ="";
//                 html += "<div id='material_chart'>";
//                 html += "<div class='message'>데이터가 없습니다.</div>";
//                 html += "</div>";
//
//                 $("#material_chart").html(html);
//
//                 html = "<div class='material-table'></div>"
//                 $(".material-table").html(html);
//             }else {
//                 var order = [];
//                 var i = 0;
//                 for(var key in response.compareDTO.diffMaterials){
//                     order[i] = key;
//                     i++;
//                 }
//                 drawMaterialChart(response.compareDTO.diffMaterials, order);
//                 drawMaterialTable(response.compareDTO, order);
//                 diffMaterialsMap = response.compareDTO.diffMaterials;
//             }
//         },
//         error: function(error) {
//             console.log(error);
//         }
//     });
// }

//
function showInit() {
    var html = "";

    html += "<div class='insert list-group'>"
    html += "<li class='list-group-item'>합금철 총 투입비용 : </li>";
    html += "<li class='list-group-item'>합금철 총 투입량 : </li>";
    html += "<li class='list-group-item'>예상 용강량 :</li>";
    html += "<li class='list-group-item'>방법 : </li>";
    html += "<div id='uploadResult'></div>";
    html += "</div>";

    $(".insert").html(html);
}


function sendRevFileName1(index) {

    console.log(index)
    $.ajax({
        url:'/api/getRevData',
        method: 'POST',
        data: { index: index },
        success: function(response) {
            // 요청이 성공한 경우의 동작

            showRevFile1(response.revResultDTO);
            //showSaveBtn();
            compareDTO = response.compareDTO;
            console.log(compareDTO);
            var html ="";
            html += "<h5 id='chart-title'>요약 지표</h5>";
            html += "<div class='sort-group'>";
            html += "<select class='sort-list' id='sort-select' style='margin-right: 10px' onchange='sendSortData()'>";
            html += "<option>정렬기준</option>";
            html += "<option>이름</option>";
            html += "<option>차이값</option>";
            html += "</select>";
            html += "<div>";
            html += "<input type='radio' id='asc' name='order' value='asc' checked onchange='sendSortData()'>오름차순";
            html += "<input type='radio' id='desc' name='order' value='desc' onchange='sendSortData()'>내림차순";
            html += "</div>";
            html += "</div>";
            $(".allchart").html(html);

            if(Object.keys(response.compareDTO.diffAlloyInputs).length === 0){
                alert("합금철별 투입량에 차이가 없습니다.");

                var html ="";
                html += "<div id='alloy_chart'>";
                html += "<div class='message'>데이터가 없습니다.</div>";
                html += "</div>";

                $("#alloy_chart").html(html);

                html = "<div class='alloy-table'></div>"
                $(".alloy-table").html(html);
            }
            else {
                var order = [];
                var i = 0;
                for(var key in response.compareDTO.diffAlloyInputs){
                    order[i] = key;
                    i++;
                }
                drawAlloyChart(response.compareDTO.diffAlloyInputs, order);
                drawAlloyTable(response.compareDTO, order);
                diffAlloyInputsMap = response.compareDTO.diffAlloyInputs;
            }
            if(Object.keys(response.compareDTO.diffMaterials).length === 0){
                alert("예상 성분에 차이가 없습니다.");
                var html = "";
                html += "<div id='material_chart'>"
                html += "<div class='message'>데이터가 없습니다.</div>";
                html += "</div>";
                $("#material_chart").html(html);

                html = "<div class='material-table'></div>"
                $(".material-table").html(html);
            }else {
                var order = [];
                var i = 0;
                for(var key in response.compareDTO.diffMaterials){
                    order[i] = key;
                    i++;
                }
                drawMaterialChart(response.compareDTO.diffMaterials, order);
                drawMaterialTable(response.compareDTO, order);
                diffMaterialsMap = response.compareDTO.diffMaterials;
            }
        },
        error: function(error) {
            console.log(error);
        }
    });
}

function showRevFile1(response) {

    console.log("리스폰스 - "+response)
    // nowIndex = response.index;

    var html = "";
    html += "<div class='insert list-group'>";
    html += "<li class='list-group-item'>합금철 총 투입비용 : " + response.totalCost + "</li>";
    html += "<li class='list-group-item'>합금철 총 투입량 : " + response.totalAmount + "</li>";
    html += "<li class='list-group-item'>예상 용강량 : " + response.expectOutput + "</li>";
    html += "<li class='list-group-item'>방법 : " + response.method + "</li>";
    html += "<div>";
    console.log("리스폰스길이 - "+response.length);
    for(var i = 0; i < response.length; i++) {
        //$("#showResult").empty();
        html += "<a href='#' id='showResult1' class='pagebtn btn m-1' onclick='sendRevFileName1(\"" + i + "\",\"" + response.index + "\", $(\"#commentRevTextarea\").val())'>" + (i + 1) + "</a>";
    }


    $(".insert").html(html);
    // nowRevComment = $("#commentRevTextarea").val();
}




//2. 삭제, 파일저장, 일괄저장 =======================================================================
// 삭제 버튼
$('.insert').on('click', '.deleteBtn', function () {
    var index = $(this).data('index');
    $.ajax({
        url: '/deleteList',
        method: 'POST',
        data: { index: index },
        success: function(response) {
            showRevFile(response);

        },
        error: function(error) {
            showInit();
        }
    });
});

// 파일 저장
// $('.hidden-group').on('click', '.saveBtn', function (){
//     showSaveModal();
// });
// function saveHistory(){
//     nowRevComment = $("#commentRevTextarea").val();
//     nowOriComment = $("#commentOriTextarea").val();
//     var title = $('#title').val();
//     //uplaod ajax
//     $.ajax({
//         url: '/saveHistory',
//         method: 'POST',
//         data: {
//             // index: nowIndex,
//             title: title
//             // revComment: nowRevComment.toString(),
//             // oriComment: nowOriComment.toString()
//         },
//         beforeSend: function () {
//             closeSaveModal();
//             alert("저장 성공");
//         },
//         success: function (result){
//             title = "";
//         },
//         error:function (error){
//             alert("기존 알고리즘이 없습니다.");
//         }
//     });
//
// }

// 파일 일괄 저장
$('.hidden-group').on('click', '.allSaveBtn', function (){
    showAllSaveModal();
});

    //원본
// function allSaveHistory(){
//     nowRevComment = $("#commentRevTextarea").val();
//     nowOriComment = $("#commentOriTextarea").val();
//     var title = $('#allSaveTitle').val();
//     //uplaod ajax
//     $.ajax({
//         url: '/allSaveHistory',
//         method: 'POST',
//         data: {
//             index: nowIndex,
//             title: title,
//             revComment: nowRevComment.toString(),
//             oriComment: nowOriComment.toString()
//         },
//         beforeSend: function () {
//             closeAllSaveModal();
//             alert("저장 성공");
//         },
//         success: function (result){
//             title = "";
//         },
//         error:function (error){
//             alert("기존 알고리즘이 없습니다.");
//         }
//     });
// }

    //수정
function allSaveHistory(){
    var title = $('#allSaveTitle').val();
    //uplaod ajax
    $.ajax({
        url: '/allSaveHistory',
        method: 'POST',
        data: {
            title: title
        },
        beforeSend: function () {
            closeAllSaveModal();
            alert("저장 성공");
        },
        success: function (result){
            title = "";
        },
        error:function (error){
            alert("기존 알고리즘이 없습니다.");
        }
    });
}









//3. 정렬 ==============================================================================
function sendSortData() {
    var selectedSort = $("#sort-select").val();
    var selectedOrder = $("input[name='order']:checked").val();
    var orderAlloyInputs = [];
    var orderMaterials = [];

    var tempAlloyInputs = new Map(Object.entries(diffAlloyInputsMap));
    var tempMaterial = new Map(Object.entries(diffMaterialsMap));

    if(selectedSort === "정렬기준") {
        var i = 0;
        for(var key in compareDTO.diffAlloyInputs){
            orderAlloyInputs[i] = key;
            i++;
        }

        var i = 0;
        for(var key in compareDTO.diffMaterials){
            orderMaterials[i] = key;
            i++;
        }
    } else if(selectedSort === "이름" && selectedOrder === "asc"){
        const orderKindAlloy = new Map([...tempAlloyInputs].sort());
        var i = 0;
        for(var [key, value] of orderKindAlloy){
            orderAlloyInputs[i] = key;
            i++;
        }

        const orderKindMaterial = new Map([...tempMaterial].sort());
        var i = 0;
        for(var [key, value] of orderKindMaterial){
            orderMaterials[i] = key;
            i++;
        }
    }
    else if(selectedSort === "이름") {
        const orderKindAlloy = new Map([...tempAlloyInputs].sort().reverse());
        var i = 0;
        for(var [key, value] of orderKindAlloy){
            orderAlloyInputs[i] = key;
            i++;
        }

        const orderKindMaterial = new Map([...tempMaterial].sort().reverse());
        var i = 0;
        for(var [key, value] of orderKindMaterial){
            orderMaterials[i] = key;
            i++;
        }
    }
    else if(selectedOrder === "asc") {
        const orderKindAlloy = new Map([...tempAlloyInputs].sort((a, b) => (a[1] > b[1] ? 1 : -1)));
        var i = 0;
        for(var [key, value] of orderKindAlloy){
            orderAlloyInputs[i] = key;
            i++;
        }

        const orderKindMaterial = new Map([...tempMaterial].sort((a, b) => (a[1] > b[1] ? 1 : -1)));
        var i = 0;
        for(var [key, value] of orderKindMaterial){
            orderMaterials[i] = key;
            i++;
        }
    }
    else {
        const orderKindAlloy = new Map([...tempAlloyInputs].sort((a, b) => (a[1] > b[1] ? -1 : 1)));
        var i = 0;
        for(var [key, value] of orderKindAlloy){
            orderAlloyInputs[i] = key;
            i++;
        }

        const orderKindMaterial = new Map([...tempMaterial].sort((a, b) => (a[1] > b[1] ? -1 : 1)));
        var i = 0;
        for(var [key, value] of orderKindMaterial){
            orderMaterials[i] = key;
            i++;
        }
    }

    if(Object.keys(compareDTO.diffAlloyInputs).length !== 0) {
        drawAlloyChart(compareDTO.diffAlloyInputs, orderAlloyInputs);
        drawAlloyTable(compareDTO, orderAlloyInputs);
        diffAlloyInputsMap = compareDTO.diffAlloyInputs;
    }

    if(Object.keys(compareDTO.diffMaterials).length !== 0) {
        drawMaterialChart(compareDTO.diffMaterials, orderMaterials);
        drawMaterialTable(compareDTO, orderMaterials);
        diffMaterialsMap = compareDTO.diffMaterials;
    }
}






//4. 표 ===============================================================================

// 합금철 투입량 표 생성 함수
function createAlloyTable(response, order) {
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


    var head, cell1, cell2;
    for (var i = 0; i < order.length; i++) {
        var key = order[i];
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



// test
// result 예상성분 표
function createMaterialTable(response, order) {
    var table = document.createElement("table");
    var caption = document.createElement("caption");
    caption.classList.add("title");
    table.appendChild(caption);

    var ori = response.oriMaterials;
    var rev = response.revMaterials;

    var totalKeys = order.length;

    if (totalKeys > 8) { // 예상 성분의 데이터가 10개 이상이면 두 개의 표로 나누어 보여줌
        var halfKeys = Math.ceil(totalKeys / 2);

        //첫번째 표
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

        var head, cell1, cell2;
        for (var i = 0; i < halfKeys; i++) {
            var key = order[i];
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
        table.appendChild(document.createElement("br")); // 첫 번째 표와 두 번째 표 사이에 빈 줄 삽입

        // 두번째표
        var row4 = document.createElement("tr");
        var row5 = document.createElement("tr");
        var row6 = document.createElement("tr");


        head = document.createElement("th");
        head.textContent = "예상 성분";
        cell1 = document.createElement("td");
        cell1.textContent = "기존 알고리즘";
        cell2 = document.createElement("td");
        cell2.textContent = "수정 알고리즘";

        row4.appendChild(head);
        row5.appendChild(cell1);
        row6.appendChild(cell2);

        for (var i = halfKeys; i < totalKeys; i++) {
            var key = order[i];
            head = document.createElement("th");
            head.textContent = key;
            cell1 = document.createElement("td");
            cell1.textContent = ori[key];
            cell2 = document.createElement("td");
            cell2.textContent = rev[key];
            row4.appendChild(head);
            row5.appendChild(cell1);
            row6.appendChild(cell2);
        }

        table.appendChild(row4);
        table.appendChild(row5);
        table.appendChild(row6);

    } else { // 예상 성분의 데이터가 10개 이하이면 하나의 표로 보여줌
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

        var head, cell1, cell2;
        for (var i = 0; i < totalKeys; i++) {
            var key = order[i];
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
    }

    return table;
}




function drawAlloyTable(response, order) {
    // 표를 추가할 컨테이너 요소
    var container = document.querySelector(".alloy-table");

    // 이전 테이블을 제거합니다.
    while (container.firstChild) {
        container.firstChild.remove();
    }

    // 합금철 투입량 표 생성
    var alloyTable = createAlloyTable(response, order);
    var alloyTableContainer = document.createElement("div");
    alloyTableContainer.className = "alloy-table";
    alloyTableContainer.appendChild(alloyTable);
    var alloyTitle = document.createElement("div");
    alloyTitle.classList.add("title");
    alloyTitle.textContent = "합금철별 투입량 :";
    container.appendChild(alloyTitle);
    container.appendChild(alloyTableContainer);

}

function drawMaterialTable(response, order) {
    // 표를 추가할 컨테이너 요소
    var container = document.querySelector(".material-table");

    // 이전 테이블을 제거합니다.
    while (container.firstChild) {
        container.firstChild.remove();
    }

    // 합금철 투입량 표 생성
    var materialTable = createMaterialTable(response, order);
    var materialTableContainer = document.createElement("div");
    materialTableContainer.className = "material-table";
    materialTableContainer.appendChild(materialTable);
    var materialTitle = document.createElement("div");
    materialTitle.classList.add("title");
    materialTitle.textContent = "result 예상 성분 :";
    container.appendChild(materialTitle);
    container.appendChild(materialTableContainer);
}



//5. 차트 =================================================================================================


// 합금철별 투입량 차트
google.charts.load('current', {'packages':['bar']});
google.charts.setOnLoadCallback();

function drawAlloyChart(response, order) {
    var data = new google.visualization.DataTable();
    // console.log(JSON.stringify(response));
    data.addColumn('string', '');
    data.addColumn('number', 'Diff');

    for(var i = 0; i < order.length; i++) {
        var key = order[i];
        var value = response[key];
        data.addRow([key, value]);
    }

    var options = {
        chart: {
            title: '합금철별 투입량',
            subtitle: '',
        },

    };

    var chart = new google.charts.Bar(document.getElementById('alloy_chart'));

    chart.draw(data, google.charts.Bar.convertOptions(options));
}





// 예상 성분 차트
google.charts.load('current', { 'packages': ['bar'] });
google.charts.setOnLoadCallback();
function drawMaterialChart(response, order) {
    var data = new google.visualization.DataTable();
    // console.log(JSON.stringify(response));
    data.addColumn('string', '');
    data.addColumn('number', 'Diff');

    // var output = response.diffAlloyInputList;
    // console.log("차트 테스트: " + output);
    for (var i = 0; i < order.length; i++) {
        var key = order[i];
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

