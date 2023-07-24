

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

    if (totalKeys > 10) { // 예상 성분의 데이터가 10개 이상이면 두 개의 표로 나누어 보여줌
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



// 차트 =============================================


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

