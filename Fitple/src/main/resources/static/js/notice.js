document.querySelector("#deleteBtn").addEventListener("click",(e)=>{
    e.preventDefault();
    e.stopPropagation();
    if(confirm("정말로 삭제하시겠습니까?")){
        let formObj = document.frm;
        formObj.action="/admin/notice/remove";
        formObj.submit();
    }
})