<script th:inline="javascript">
// 템플릿 엔진이 javascript 코드를 인라인으로 처리하도록 설정
    const alertMsg = /*[[${alertMsg}]]*/ null;
    // 타임리프의 데이터 바인딩을 통해 서버에서 전달된 alertMsg 변수를 javascript 변수 alertMsg에 바인딩됨
    if(alertMsg && alertMsg.trim(.length > 0 )){
        alert(alertMsg.trim());
        // alertMsg가 0보다 크면 출력
    }
    history.back();
</script>
// 주석으로 처리한 이유는 타임리프 템플릿 엔진은 서버측에서 클라이언트 측으로 데이터를 전달할 수 있다.
// 주석을 처리된 부분은 타임리프의 데이터 바인딩 구문을 나타내며, 이러한 표현식은 클라이언트에 직접 노출이 안됨
// 이렇게 처리함으로써 javasccript 코드가 클라이언트에 노출될 때 서버측 데이터가 노출되지 않도록 보호가 가능하다
// 보안상의 이유로 일반적으로 권장되는 접근 방식이다.
