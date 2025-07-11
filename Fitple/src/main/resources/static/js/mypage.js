

    const modalWrapper = document.getElementById('modal-wrapper');
    const modalBody = document.getElementById('modal-body');
    const closeModalBtn = document.getElementById('close-modal-btn');

    closeModalBtn.addEventListener('click', () => {
    modalWrapper.style.display = 'none';
});

    modalWrapper.addEventListener('click', (e) => {
    if (e.target === modalWrapper) modalWrapper.style.display = 'none';
});

    function showHousingDetail(houseData) {
    const check = val => val ?? '정보 없음';
    const format = val => Number(val || 0).toLocaleString('ko-KR') + ' 원';

    modalBody.innerHTML = `
      <table>
        <tr><th>단지명</th><td>${check(houseData.hsmpNm)}</td></tr>
        <tr><th>주소</th><td>${check(houseData.rnAdres)}</td></tr>
        <tr><th>유형</th><td>${check(houseData.houseTyNm)}</td></tr>
        <tr><th>전용면적</th><td>${check(houseData.suplyPrvuseAr)} ㎡</td></tr>
        <tr><th>보증금</th><td>${format(houseData.bassRentGtn)}</td></tr>
        <tr><th>월세</th><td>${format(houseData.bassMtRntchrg)}</td></tr>
        <tr><th>기관</th><td>${check(houseData.insttNm)}</td></tr>
      </table>
    `;
    modalWrapper.style.display = 'flex';
}

    // 이벤트 바인딩
    window.addEventListener('DOMContentLoaded', () => {
    document.querySelectorAll('li[data-house-info]').forEach(li => {
        const raw = li.getAttribute('data-house-info');
        try {
            const houseData = JSON.parse(raw.replace(/&quot;/g, '"'));  // JSON 문자열 복원
            li.addEventListener('click', () => showHousingDetail(houseData));
        } catch (e) {
            console.error('주거정보 파싱 오류:', e);
        }
    });
});
