function sendPostRequest() {
    // 폼 요소를 생성합니다.
    const form = document.createElement('form');
    form.method = 'post'; // 폼의 메서드를 POST로 설정합니다.
    form.action = '/user/info'; // 폼의 액션을 '/user/info'로 설정합니다.

    // CSRF 토큰 입력 필드를 생성합니다.
    const csrfTokenInput = document.createElement('input');
    csrfTokenInput.type = 'hidden'; // 입력 필드의 타입을 숨김으로 설정합니다.
    csrfTokenInput.name = /*[[${_csrf.parameterName}]]*/ '_csrf'; // CSRF 토큰의 파라미터 이름을 설정합니다.
    csrfTokenInput.value = /*[[${_csrf.token}]]*/ ''; // CSRF 토큰의 값을 설정합니다.

    // CSRF 토큰 입력 필드를 폼에 추가합니다.
    form.appendChild(csrfTokenInput);
    // 생성한 폼을 문서의 body에 추가합니다.
    document.body.appendChild(form);
    // 폼을 제출합니다.
    form.submit();
}
