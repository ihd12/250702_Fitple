
   // 커스텀 오버레이를 위한 객체 생성
    var placeOverlay = new kakao.maps.CustomOverlay({ zIndex: 1 }),
    contentNode = document.createElement('div'), // 커스텀 오버레이에 표시할 내용
    markers = [],              // 지도에 표시된 마커들을 담는 배열
    currCategory = '',         // 현재 선택된 카테고리 코드 (예: 'CE7' = 카페)
    currKeyword = '';          // 현재 입력된 키워드 문자열

    // 지도 생성
    var mapContainer = document.getElementById('map'),
    mapOption = {
    center: new kakao.maps.LatLng(37.566826, 126.9786567), // 서울 시청 기준
    level: 5
};
    var map = new kakao.maps.Map(mapContainer, mapOption); // 지도 객체 생성

    // 장소 검색 서비스 객체 생성
    var ps = new kakao.maps.services.Places(map);

    // 지도 상태가 바뀌었을 때 (idle), 검색 실행
    kakao.maps.event.addListener(map, 'idle', searchPlaces);

    // 커스텀 오버레이 스타일 및 이벤트 등록
    contentNode.className = 'placeinfo_wrap';
    addEventHandle(contentNode, 'mousedown', kakao.maps.event.preventMap);
    addEventHandle(contentNode, 'touchstart', kakao.maps.event.preventMap);
    placeOverlay.setContent(contentNode);

    // 카테고리 버튼에 이벤트 연결
    addCategoryClickEvent();

    // 이벤트 핸들러 등록 함수 (브라우저 호환용)
    function addEventHandle(target, type, callback) {
    if (target.addEventListener) {
    target.addEventListener(type, callback);
} else {
    target.attachEvent('on' + type, callback);
}
}

    // 키워드 기반 장소 검색
    function searchKeywordPlaces(keyword) {
    if (!keyword.trim()) return; // 공백이면 무시
    placeOverlay.setMap(null);
    removeMarker();

    ps.keywordSearch(keyword, placesSearchCB, { useMapBounds: true }); // 지도 범위 내 검색
}

    // 카테고리 또는 키워드에 따라 검색
    function searchPlaces() {
    if (currKeyword) {
    searchKeywordPlaces(currKeyword); // 키워드가 있으면 키워드 우선
    return;
}

    if (!currCategory) return;

    placeOverlay.setMap(null);
    removeMarker();

    ps.categorySearch(currCategory, placesSearchCB, { useMapBounds: true });
}

    // 검색 결과 콜백 함수
    function placesSearchCB(data, status, pagination) {
    if (status === kakao.maps.services.Status.OK) {
    console.log("✅ 카카오 API 응답 데이터:", data);
    displayPlaces(data); // 성공 시 결과 표시
} else{
    console.error("❌ 검색 실패 상태:", status);
}
}

    // 장소 리스트와 마커 표시
    function displayPlaces(places) {
    var listEl = document.getElementById('placesList');
    listEl.innerHTML = ''; // 기존 리스트 초기화

    document.getElementById('resultCount').innerText = `총 ${places.length}개 장소`;

    var useDefaultMarker = !currCategory; // 카테고리 없으면 기본 마커 사용
    var order = '';

    if (currCategory) {
    order = document.getElementById(currCategory).getAttribute('data-order');
}

    for (var i = 0; i < places.length; i++) {
    // 마커 생성
    var marker = addMarker(new kakao.maps.LatLng(places[i].y, places[i].x), order, useDefaultMarker);

    // 리스트 항목 생성
    var itemEl = document.createElement('li');
    itemEl.innerHTML = `<strong>${places[i].place_name}</strong><br>${places[i].address_name}`;
    listEl.appendChild(itemEl);

    // 마커 클릭 및 리스트 클릭 시 오버레이 표시
    (function (marker, place, listItem) {
    kakao.maps.event.addListener(marker, 'click', function () {
    displayPlaceInfo(place);
    highlightListItem(listItem);
});

    listItem.onclick = function () {
    kakao.maps.event.trigger(marker, 'click');
};
})(marker, places[i], itemEl);
}
}

    // 마커 추가 함수 (기본 마커 or 커스텀 아이콘)
    function addMarker(position, order, useDefaultMarker) {
    var marker = new kakao.maps.Marker({
    position: position
});
    marker.setMap(map);
    markers.push(marker);
    return marker;
}

    // 기존 마커 전부 제거
    function removeMarker() {
    for (var i = 0; i < markers.length; i++) {
    markers[i].setMap(null);
}
    markers = [];
}

    // 장소 정보 오버레이 표시
    function displayPlaceInfo(place) {
    var content = '<div class="placeinfo">' +
    `<a class="title" href="${place.place_url}" target="_blank">${place.place_name}</a>`;

    if (place.road_address_name) {
    content += `<span>${place.road_address_name}</span><span class="jibun">(지번: ${place.address_name})</span>`;
} else {
    content += `<span>${place.address_name}</span>`;
}

    content += `<span class="tel">${place.phone}</span></div><div class="after"></div>`;

    contentNode.innerHTML = content;
    placeOverlay.setPosition(new kakao.maps.LatLng(place.y, place.x));
    placeOverlay.setMap(map);
}

    // 리스트 항목 강조 표시
    function highlightListItem(el) {
    let items = document.querySelectorAll('#placesList li');
    items.forEach(item => item.classList.remove('selected'));
    el.classList.add('selected');
}

    // 카테고리 버튼 이벤트 등록
    function addCategoryClickEvent() {
    var category = document.getElementById('category'),
    children = category.children;

    for (var i = 0; i < children.length; i++) {
    children[i].onclick = onClickCategory;
}
}

    // 카테고리 클릭 시 처리
    function onClickCategory() {
    var id = this.id,
    className = this.className;

    placeOverlay.setMap(null);

    if (className === 'on') {
    currCategory = '';
    changeCategoryClass();
    removeMarker();
} else {
    currCategory = id;
    currKeyword = ''; // 키워드 초기화
    document.getElementById('keyword').value = '';
    changeCategoryClass(this);
    searchPlaces();
}
}

    // 선택된 카테고리만 'on' 클래스 적용
    function changeCategoryClass(el) {
    var category = document.getElementById('category'),
    children = category.children;

    for (var i = 0; i < children.length; i++) {
    children[i].className = '';
}

    if (el) {
    el.className = 'on';
}
}

    // 키워드 검색창: 엔터 키 입력 처리
    document.getElementById('keyword').addEventListener('keydown', function (e) {
    if (e.key === 'Enter') {
    currKeyword = this.value;          // 입력값 저장
    currCategory = '';                 // 카테고리 선택 해제
    changeCategoryClass();             // 버튼 스타일 초기화
    searchPlaces();                    // 키워드 검색 실행
}
});

    var geocoder = new kakao.maps.services.Geocoder();

    function searchAddress() {

    const Pkeyword = document.getElementById('Pkeyword').value;

    geocoder.addressSearch(Pkeyword, function(result, status) {
    if (status === kakao.maps.services.Status.OK) {
    var coords = new kakao.maps.LatLng(result[0].y, result[0].x);

    map.setCenter(coords);  // 지도 중심 이동
    map.setLevel(5);        // 확대 레벨 조절

    new kakao.maps({
    map: map,
    position: coords
});
} else {
    alert("해당 지역을 찾을 수 없습니다.");
}
});
}
    // 예시: 버튼 클릭 또는 select onchange에서 호출
    moveToAddress("수원시 영통구");

    fetch(`/api/boundary?region=${encodeURIComponent(Pkeyword)}`)
    .then(res => res.json())
    .then(data => {
    const path = data.map(coord => new kakao.maps.LatLng(coord[1], coord[0]));

    const polygon = new kakao.maps.Polygon({
    path: path,
    strokeWeight: 2,
    strokeColor: '#004c80',
    strokeOpacity: 0.8,
    fillColor: '#00a0e9',
    fillOpacity: 0.3
});

    polygon.setMap(map);
});