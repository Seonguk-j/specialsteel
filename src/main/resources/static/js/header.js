

$(document).ready(function() {
    // 버튼 클릭 이벤트 핸들러
    $(".nav-link").on('click', function(event) {
        $(".nav-link").removeClass('active'); // 모든 버튼의 활성화 클래스를 제거합니다.
        $(this).addClass('active'); // 클릭한 버튼에 활성화 클래스를 추가합니다.

        // 현재 활성화된 버튼의 ID를 sessionStorage에 저장합니다.
        sessionStorage.setItem('act', this.id);
    });

    // Navbar 브랜드 클릭 이벤트 핸들러
    $(".navbar-brand").on('click', function(event) {
        $(".nav-link").removeClass('active'); // 모든 버튼의 활성화 클래스를 제거합니다.
        $("#button1").addClass('active'); // 버튼 1을 활성화 상태로 설정합니다.

        sessionStorage.clear();
        sessionStorage.setItem('act', 'button1');
    });

    // 페이지 로드 시, sessionStorage에 저장된 버튼의 ID를 확인하여 활성화 상태를 복원합니다.
    if (sessionStorage.getItem('act') != null) {
        let id = sessionStorage.getItem('act');
        $('#' + id).addClass('active');
    } else {
        $("#button1").addClass('active'); // 초기 활성화 버튼을 설정합니다.
        sessionStorage.setItem('act', 'button1');
    }

    // 출력 페이지에서 변환 페이지로 직접 링크를 클릭한 경우, 변환 페이지에 대한 버튼 효과를 출력 페이지에서 유지합니다.
    var currentURL1 = window.location.href;
    if (currentURL1.includes("encoding")) {
        $(".nav-link").removeClass('active');
        $("#button2").addClass('active');
        sessionStorage.setItem('act', 'button2');
    }
    // 변환 페이지에서 출력 페이지로 직접 링크를 클릭한 경우, 출력 페이지에 대한 버튼 효과를 변환 페이지에서 유지합니다.
    var currentURL2 = window.location.href;
    if (currentURL2.includes("result")) {
        $(".nav-link").removeClass('active');
        $("#button1").addClass('active');
        sessionStorage.setItem('act', 'button1');
    }
});

