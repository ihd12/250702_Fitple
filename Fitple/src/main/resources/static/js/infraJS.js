'use strict';

// 전역변수(마커, 카테고리, 키워드) 및 지도 초기화
var placeOverlay = new kakao.maps.CustomOverlay({ zIndex: 1 }),
    contentNode = document.createElement('div'),
    markers = [],
    currCategory = '',
    currKeyword = '';

// 기본 지도 중심 좌표와 줌 레벨 설정
const mapContainer = document.getElementById('map');
const map = new kakao.maps.Map(mapContainer, {
    center: new kakao.maps.LatLng(37.566826, 126.9786567),
    level: 7
});

// 장소 검색 서비스를 위한 객체 생성
var ps = new kakao.maps.services.Places(map);

// contentNode : 장소 상세정보를 담을 DOM 노드
contentNode.className = 'placeinfo_wrap';
// 지도 기본 이벤트 방지(클릭 시 지도 드래그 방지)
addEventHandle(contentNode, 'mousedown', kakao.maps.event.preventMap);
addEventHandle(contentNode, 'touchstart', kakao.maps.event.preventMap);
placeOverlay.setContent(contentNode);

// 이벤트 핸들러 등록 함수 (호환성용)
function addEventHandle(target, type, callback) {
    if (target.addEventListener) {
        target.addEventListener(type, callback);
    } else {
        target.attachEvent('on' + type, callback);
    }
}

// --------------------------- 키워드 기반 장소 검색-----------------------
function searchKeywordPlaces(keyword) {
    if (!keyword.trim()) return;
    // 장소 검색시 기존 마커, 오버레이 제거
    placeOverlay.setMap(null);
    removeMarker();

    // 키워드 기반 검색 수행
    ps.keywordSearch(keyword, placesSearchCB, { useMapBounds: true });
}

// 카테고리 또는 키워드에 따라 검색
function searchPlaces() {
    // 키워드가 없고 카테고리가 있으면 카테고리 검색, 둘다 없을시 실행 X
    if (currKeyword) {
        searchKeywordPlaces(currKeyword);
        return;
    }
    if (!currCategory && !currKeyword) return;

    placeOverlay.setMap(null);
    removeMarker();

    ps.categorySearch(currCategory, placesSearchCB, { useMapBounds: true });
}

// 장소 검색 결과 콜백 함수
function placesSearchCB(data, status, pagination) {
    // 검색 성공시
    if (status === kakao.maps.services.Status.OK) {
        if (data.length === 0) {
            // 검색 결과가 없을때 리스트, 페이징,마커,오버레이 초기화
            alert("검색된 장소가 없습니다.");
            document.getElementById('placesList').innerHTML = '';
            document.getElementById('pagination').innerHTML = '';
            removeMarker();
            placeOverlay.setMap(null);
            return;
        }

        // 정상 결과일 경우: 장소 리스트와 페이징 생성
        displayPlaces(data);
        displayPagination(pagination);
    } else if (status === kakao.maps.services.Status.ZERO_RESULT) {
        // 장소가 없는 경우
        alert("검색된 장소가 없습니다.");
        document.getElementById('placesList').innerHTML = '';
        document.getElementById('pagination').innerHTML = '';
        removeMarker();
        placeOverlay.setMap(null);
    } else {
        alert('장소 검색 중 오류가 발생했습니다.');
        console.error("❌ 검색 실패 상태:", status);
    }
}

// 검색 결과 장소 리스트와 마커 표시
function displayPlaces(places) {
    var listEl = document.getElementById('placesList');
    listEl.innerHTML = '';

    var useDefaultMarker = !currCategory;


    for (var i = 0; i < places.length; i++) {
        // 마커 생성 및 지도에 표시
        var marker = addMarker(new kakao.maps.LatLng(places[i].y, places[i].x), useDefaultMarker);

        // 리스트 항목 생성
        var itemEl = document.createElement('li');
        itemEl.innerHTML = `<strong>${places[i].place_name}</strong><br>${places[i].address_name}`;
        listEl.appendChild(itemEl);

        // 마커와 리스트 항목에 이벤트 연결 (즉시 실행 함수 사용)
        (function (marker, place, listItem) {
            // 마커 클릭 시 → 오버레이 표시 + 리스트 강조
            kakao.maps.event.addListener(marker, 'click', function () {
                displayPlaceInfo(place);
                highlightListItem(listItem);
            });

            // 리스트 항목 클릭 시 → 마커 클릭 이벤트 발생 + 지도 이동
            listItem.onclick = function () {
                kakao.maps.event.trigger(marker, 'click'); // 마커 클릭 이벤트 강제 실행
                map.panTo(marker.getPosition());                // 지도 중심 이동
                document.getElementById('map').scrollIntoView({ behavior: 'smooth' });
            };
        })(marker, places[i], itemEl); // 클로저로 값 고정
    }
}

// 마커 생성 후 지도에 표시
function addMarker(position) {
    var marker = new kakao.maps.Marker({ position: position });
    marker.setMap(map);
    markers.push(marker);
    return marker;
}

// 기존 마커 제거
function removeMarker() {
    for (var i = 0; i < markers.length; i++) {
        markers[i].setMap(null);
    }
    markers = [];
}

// -----------------특정 장소 클릭 시 상세정보 표시------------------
function displayPlaceInfo(place) {
    // 상세 정보
    var content = '<div class="placeinfo">' +
        `<a class="title" href="${place.place_url}" target="_blank">${place.place_name}</a>`;

    // 주소 정보 표시(도로명 주소 유무)
    if (place.road_address_name) {
        content += `<span>${place.road_address_name}</span><span class="jibun">(지번: ${place.address_name})</span>`;
    } else {
        content += `<span>${place.address_name}</span>`;
    }
    // 전화번호
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

//-----------------------------------카테고리----------------------------------
// 카테고리 버튼 이벤트 등록
function addCategoryClickEvent() {
    var category = document.getElementById('category'),
        children = category.children;

    for (var i = 0; i < children.length; i++) {
        children[i].onclick = onClickCategory;
    }
}

// 카테고리 클릭 처리
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
        currKeyword = '';
        document.getElementById('keyword').value = '';
        changeCategoryClass(this);
        searchPlaces();
    }
}

// 선택된 카테고리만 on 클래스 적용(강조)
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

//---------------------------------페이징----------------------------------
function displayPagination(pagination) {
    const paginationEl = document.getElementById('pagination');
    paginationEl.innerHTML = '';

    const fragment = document.createDocumentFragment();

    for (let i = 1; i <= pagination.last; i++) {
        const page = document.createElement('a');
        page.href = '#';
        page.innerText = i;

        if (i === pagination.current) {
            page.className = 'on';
        } else {
            page.onclick = (function (i) {
                return function () {
                    pagination.gotoPage(i);
                };
            })(i);
        }
        fragment.appendChild(page);
    }
    paginationEl.appendChild(fragment);
}
// ----------------------------kakao 주소 검색 ----------------------------
var geocoder = new kakao.maps.services.Geocoder();
let circleOverlay = null;
function searchAddress() {
    const Pkeyword = document.getElementById('Pkeyword').value.trim();

    if (!Pkeyword) {
        alert("지역명을 입력하세요.");
        return;
    }
    // kakao API 주소 검색 실행
    geocoder.addressSearch(Pkeyword, function (result, status) {
        if (status === kakao.maps.services.Status.OK) {
            var coords = new kakao.maps.LatLng(result[0].y, result[0].x);

            currKeyword = '';
            currCategory = '';
            changeCategoryClass(); // 카테고리 선택 해제
            document.getElementById("keyword").value = ''; // 키워드 입력창 초기화

            map.setCenter(coords);
            map.setLevel(3);


            if (currKeyword && currKeyword.trim() !== '') {
                searchKeywordPlaces(currKeyword);
            } else if (currCategory) {
                searchPlaces();
            } else {
                removeMarker();
                placeOverlay.setMap(null);
                document.getElementById('placesList').innerHTML = '';
                document.getElementById('pagination').innerHTML = '';
            }
        } else {
            alert("해당 지역을 찾을 수 없습니다.");
            currKeyword = '';
            return;
        }
    });
}
// ----------------------------------경계선 표시----------------------------------
// 시/군/구 폴리곤 경계선 표시
let polygon = null;

document.getElementById('sigunguSelect').addEventListener('change', function () {
    const sigCd = this.value;

    if (!sigCd) return;

    // 기존 폴리곤 제거
    if (polygon) {
        polygon.setMap(null);
        polygon = null;
    }

    // ✅ 기존 원형 오버레이 제거 ← 이거 추가!
    if (circleOverlay) {
        circleOverlay.setMap(null);
        circleOverlay = null;
    }
    fetch(`/api/polygon?sigCd=${sigCd}`)
        .then(res => res.json())
        .then(data => {
            const features = data.response?.result?.featureCollection?.features;

            if (!features || features.length === 0) {
                alert("해당 지역 데이터가 없습니다.");
                return;
            }

            const coords = features[0].geometry.coordinates;
            let paths = [];

            if (features[0].geometry.type === "MultiPolygon") {
                coords.forEach(polygonCoords => {
                    polygonCoords.forEach(ring => {
                        const path = ring.map(([lng, lat]) => new kakao.maps.LatLng(lat, lng));
                        paths.push(path);
                    });
                });
            } else if (features[0].geometry.type === "Polygon") {
                coords.forEach(ring => {
                    const path = ring.map(([lng, lat]) => new kakao.maps.LatLng(lat, lng));
                    paths.push(path);
                });
            } else {
                alert("폴리곤 데이터가 아닙니다.");
                return;
            }

            function getPolygonCenter(paths) {
                let sumLat = 0, sumLng = 0, count = 0;
                paths.forEach(path => {
                    path.forEach(latlng => {
                        sumLat += latlng.getLat();
                        sumLng += latlng.getLng();
                        count++;
                    });
                });
                return new kakao.maps.LatLng(sumLat / count, sumLng / count);
            }

            const center = getPolygonCenter(paths);
            map.setCenter(center);
            map.setLevel(5);

            polygon = new kakao.maps.Polygon({
                map: map,
                path: paths,
                strokeWeight: 3,
                strokeColor: '#004c80',
                strokeOpacity: 0.5,
                fillColor: '#00a0e9',
                fillOpacity: 0.1
            });
        })
        .catch(err => {
            console.error(err);
            alert("폴리곤 데이터를 불러오는 중 오류가 발생했습니다.");
        });
});
// 읍/면/동 원형 오버레이 표시
function moveToAddress(address, drawCircle = true) {
    geocoder.addressSearch(address, function (result, status) {
        if (status === kakao.maps.services.Status.OK) {
            const coords = new kakao.maps.LatLng(result[0].y, result[0].x);

            currKeyword = '';
            currCategory = '';
            changeCategoryClass();
            document.getElementById("keyword").value = '';

            map.setCenter(coords);
            map.setLevel(3);

            if (polygon) {
                polygon.setMap(null);
                polygon = null;
            }

            // ✅ drawCircle이 true일 때만 원 생성
            if (circleOverlay) {
                circleOverlay.setMap(null);
                circleOverlay = null;
            }

            if (drawCircle) {
                circleOverlay = new kakao.maps.Circle({
                    center: coords,
                    radius: 1000,
                    strokeWeight: 3,
                    strokeColor: '#004c80',
                    strokeOpacity: 0.8,
                    fillColor: '#00a0e9',
                    fillOpacity: 0.1
                });
                circleOverlay.setMap(map);
            }

            // 키워드/카테고리 검색 처리
            if (currKeyword && currKeyword.trim() !== '') {
                searchKeywordPlaces(currKeyword);
            } else if (currCategory) {
                searchPlaces();
            } else {
                removeMarker();
                placeOverlay.setMap(null);
                document.getElementById('placesList').innerHTML = '';
                document.getElementById('pagination').innerHTML = '';
            }
        } else {
            alert("해당 지역을 찾을 수 없습니다.");
        }
    });
}
// -------------------------------시/도 , 시/군/구 목록 받아오기 -------------------------------
    const sidoSelect = document.getElementById("sidoSelect");
    const sigunguSelect = document.getElementById("sigunguSelect");
    const PkeywordInput = document.getElementById("Pkeyword");
    const keywordInput = document.getElementById("keyword");

    // 시/도 데이터 목록 받아오기
    fetch("/api/sido")
        .then(res => res.json())
        .then(data => {
            const features = data.response.result.featureCollection.features;
            const sidoList = features.map(f => ({
                code: f.properties.ctprvn_cd,
                name: f.properties.ctp_kor_nm
            }));

            sidoList.forEach(sido => {
                const option = document.createElement("option");
                option.value = sido.code;
                option.textContent = sido.name;
                sidoSelect.appendChild(option);
            });
        });

    // 시/도 선택시 해당 시/군/구 데이터 받아오기
    sidoSelect.addEventListener("change", function () {
        const sidoCd = this.value;
        sigunguSelect.innerHTML = "<option value=''>시/군/구 선택</option>";

        if (!sidoCd) {
            PkeywordInput.value = "";
            return;
        }
        fetch(`/api/sigungu?sidoCd=${sidoCd}`)
    .then(res => res.json())
            .then(data => {
                const features = data.response.result.featureCollection.features;
                const sigunguList = features.map(f => ({
                    code: f.properties.sig_cd,
                    name: f.properties.sig_kor_nm
                }));

                sigunguList.forEach(sig => {
                    const option = document.createElement("option");
                    option.value = sig.code;
                    option.textContent = sig.name;
                    sigunguSelect.appendChild(option);
                });
            });
    });
    // 시/군/구 선택시 실행. 해당 지도 중심으로 이동
    sigunguSelect.addEventListener("change", function () {
        const sidoText = sidoSelect.options[sidoSelect.selectedIndex].text;
        const sigunguText = this.options[this.selectedIndex].text;

        if (sidoText && sigunguText) {
            PkeywordInput.value = `${sidoText} ${sigunguText}`;

            moveToAddress(`${sidoText} ${sigunguText}`, false);
        }
    });
    //-------------------------- 검색 엔터키 기능 처리--------------------------
    // 장소명 입력창 엔터로 검색하는 기능
    keywordInput.addEventListener("keydown", function (e) {
        if (e.key === "Enter") {
            e.preventDefault();
            if (!this.value.trim()) {
                alert("장소명을 입력하세요");
                return;
            }

            currKeyword = this.value;
            currCategory = '';
            changeCategoryClass(); // 카테고리 선택 해제
            searchPlaces();        // 검색 실행
        }
    });
    // 지역명 입력창 엔터로 검색하는 기능
    PkeywordInput.addEventListener("keydown", function (e) {
        if (e.key === "Enter") {
            e.preventDefault();
            if (!this.value.trim()) {
                alert("지역명을 입력하세요");
                return;
            }
           moveToAddress(this.value, true); // 엔터 시 주소 검색 실행
        }
    });

// 카테고리 버튼 이벤트 초기화
addCategoryClickEvent();
