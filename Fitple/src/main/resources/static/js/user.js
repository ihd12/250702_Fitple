document.querySelector("#removeBtn").addEventListener("click",(e)=>{
    e.preventDefault();
    e.stopPropagation();
    if (confirm("정말로 회원을 탈퇴하시겠습니까?")) {
        let formObj = document.frm;
        formObj.action = "/user/remove"
        formObj.submit();
        alert('회원 탈퇴가 완료되었습니다.');
    }
})
document.querySelector("#nickBtn").addEventListener("click",(e)=>{
    e.preventDefault();
    e.stopPropagation();
    let formObj = document.frm;
    formObj.action="/user/modify"
    formObj.password.value = "";
    formObj.submit();
    alert(`닉네임이 변경되었습니다.`);
})
document.querySelector("#pwBtn").addEventListener("click",(e)=>{
    e.preventDefault();
    e.stopPropagation();
    let formObj = document.frm;
    let pw = formObj.password.value;
    let pw_confirm = document.querySelector("#pw_confirm").value;
    if(pw !== pw_confirm){
        alert("비밀번호를 확인해주세요!");
        return;
    }
    formObj.action="/user/modify"
    formObj.nickname.value = "";
    formObj.submit();
    alert('비밀번호가 변경되었습니다.');
})
//     http://localhost:8080/user/modify

