<script th:inline="javascript">
    // 일반 메세지
    // 로컬 스토리지 키 및 메시지들을 가져옴
    const localStorageKeyAboutHistoryBackMsg = /*[[${localStorageKeyAboutHistoryBackMsg}]]*/ null;
    const historyBackMsg = /*[[${historyBackMsg}]]*/ null;

        // 로컬 스토리지에 이전 페이지로 돌아갈 때 메시지를 저장
      if (localStorageKeyAboutHistoryBackMsg && localStorageKeyAboutHistoryBackMsg.trim().length > 0) {
           localStorage.setItem(localStorageKeyAboutHistoryBackMsg, historyBackMsg);
       }

       // 에러 메세지
       // 에러 메시지와 관련된 로컬 스토리지 키 및 메시지들을 가져옴
       const localStorageKeyAboutHistoryBackErrorMsg = /*[[${localStorageKeyAboutHistoryBackErrorMsg}]]*/ null;
       const historyBackErrorMsg = /*[[${historyBackErrorMsg}]]*/ null;

        // 로컬 스토리지에 에러 메시지를 저장
       if (localStorageKeyAboutHistoryBackErrorMsg && localStorageKeyAboutHistoryBackErrorMsg.trim().length > 0) {
           localStorage.setItem(localStorageKeyAboutHistoryBackErrorMsg, historyBackErrorMsg);
    }
    // 위 코드는 javascript를 사용하여 로컬 스토리지에서 데이터를 가져오고 저장하는 기능을 수행
    // localStorageKeyAboutHistoryBackMsg 및 historyBackMsg는 이전 페이지로 돌아갈 때 메시지를 저장하는데 사용되는 키 및 메시지
    // localStorageKeyAboutHistoryBackErrorMsg 및 historyBackErrorMsg는 에러 메시지를 저장하는 데 사용


    history.back();
</script>
// 주석으로 처리한 이유는 타임리프 템플릿 엔진은 서버측에서 클라이언트 측으로 데이터를 전달할 수 있다.
// 주석을 처리된 부분은 타임리프의 데이터 바인딩 구문을 나타내며, 이러한 표현식은 클라이언트에 직접 노출이 안됨
// 이렇게 처리함으로써 javasccript 코드가 클라이언트에 노출될 때 서버측 데이터가 노출되지 않도록 보호가 가능하다
// 보안상의 이유로 일반적으로 권장되는 접근 방식이다.
