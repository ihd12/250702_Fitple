// http://localhost:8080/admin/member
function deleteMember(data){
    let email = data.getAttribute("data-email");
    if(!confirm(`${email}회원을 삭제하시겠습니까?`)){
        return;
    }
    fetch(`/admin/member`, {
        method:'DELETE',
        headers:{'Content-Type' : 'application/json'},
        body: JSON.stringify({email:email})
    }).then((response)=>{
        if(response.ok){
            let deleteRow = data.getAttribute("data-id");
            let row = document.getElementById(`row-${deleteRow}`);
            if(row){row.remove();}
            alert('회원을 삭제했습니다.');
        }else{
            alert('회원 삭제에 실패했습니다. 서버 오류');
        }
    }).catch(error=>{
        console.error("회원 삭제 에러",error);
        alert("회원 삭제에 실패했습니다. 통신 오류")
    })
}