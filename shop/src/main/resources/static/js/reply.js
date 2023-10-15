console.log("javascript called!!!")

// 비동기처리를 위한 함수 선언
 async function get1(bno){
    console.log("request bno:"+bno)

    //console.log(`/replies/list/${bno}`)

    // await는 async함수 내에서 비동기 호출하는 부분
    const result = await axios.get(`/replies/list/${bno}`)

    //console.log(result);

    return result;
}



// 댓글 목록 처리
// bno:현재 게시글번호, page:페이지번호, size:페이지당 사이즈, goLast:마지막페이지 호출여부
// 특정 게시글에 대한 댓글 목록 조회요청
async function getList({bno, page, size}){
   
    const result = await axios.get(`/replies/list/${bno}`,{params: {page,size}})


    return result.data;
}
// 댓글 등록
async function addReply(replyObj){
   
    const response = await axios.post(`/replies/`, replyObj)


    return response.data;
}

// 특정 댓글 조회
async function getReply(rno){

    const response = await axios.get(`/replies/${rno}`)

    return response.data;
}
// 특정 댓글 수정
async function modifyReply(replyObj){

    const response = await axios.put(`/replies/${replyObj.rno}`, replyObj)

    return response.data;
}

// 특정 댓글 삭제
async function removeReply(rno){

    const response = await axios.delete(`/replies/${rno}`)

    return response.data;
}