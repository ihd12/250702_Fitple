// 현재 모달의 pnu를 저장할 변수
let currentPnuForModal = null;

// (수정) 모달 표시 함수
function populateAndShowModal(pnu) {
    currentPnuForModal = pnu; // 현재 pnu 저장
    // ... 기존 데이터 바인딩 코드 ...

    // 기존 innerHTML 코드 끝에 댓글 섹션 HTML 추가
    modalBody.innerHTML = `...` // 위에 제시된 HTML 구조 포함

    // 모달을 표시한 후 댓글 목록 가져오기
    modalWrapper.style.display = 'flex';
    fetchAndRenderReplies(pnu);

    // 댓글 등록 버튼에 이벤트 리스너 추가
    document.getElementById('submit-reply-btn').addEventListener('click', submitReply);
}


// API로부터 댓글을 가져와 화면에 그리는 함수
async function fetchAndRenderReplies(pnu) {
    const replyListDiv = document.getElementById('reply-list');
    replyListDiv.innerHTML = '댓글을 불러오는 중...'; // 로딩 표시

    try {
        const response = await fetch(`/api/replies?pnu=${pnu}`);
        if (!response.ok) throw new Error('댓글을 불러오는데 실패했습니다.');

        const replies = await response.json();
        replyListDiv.innerHTML = ''; // 로딩 표시 제거

        if (replies.length === 0) {
            replyListDiv.innerHTML = '<p>작성된 댓글이 없습니다.</p>';
            return;
        }

        replies.forEach(reply => {
            const replyEl = document.createElement('div');
            replyEl.className = 'reply-item';
            // data-id 속성에 댓글 ID 저장 (수정/삭제 시 사용)
            replyEl.setAttribute('data-id', reply.id);

            replyEl.innerHTML = `
                <p class="reply-author">${reply.userId}</p>
                <p class="reply-content">${reply.replyContent}</p>
                <span class="reply-date">${new Date(reply.createdAt).toLocaleString()}</span>
                <button class="edit-reply-btn">수정</button>
                <button class="delete-reply-btn">삭제</button>
            `;
            replyListDiv.appendChild(replyEl);
        });
    } catch (error) {
        replyListDiv.innerHTML = `<p>${error.message}</p>`;
    }
}

// 새 댓글을 등록하는 함수
async function submitReply() {
    const contentInput = document.getElementById('reply-content-input');
    const replyContent = contentInput.value.trim();

    if (!replyContent) {
        alert('댓글 내용을 입력하세요.');
        return;
    }

    try {
        const response = await fetch('/api/replies', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                pnu: currentPnuForModal,
                replyContent: replyContent
            })
        });

        if (!response.ok) throw new Error('댓글 등록에 실패했습니다.');

        contentInput.value = ''; // 입력창 비우기
        fetchAndRenderReplies(currentPnuForModal); // 댓글 목록 새로고침

    } catch (error) {
        alert(error.message);
    }
}
