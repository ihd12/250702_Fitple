// housing.js 파일 상단

// 1. HTML의 data-* 속성에서 값을 가져옵니다.
const bodyEl = document.body;
const isLoggedIn = bodyEl.dataset.isAuthenticated === 'true';
let userId = bodyEl.dataset.userId;

// 2. userId가 문자열 'null'이면 실제 null 값으로 변경합니다.
if (userId === 'null') {
    userId = null;
}

console.log('로그인 상태:', isLoggedIn);
console.log('유저 ID:', userId);

// 찜하기 버튼 상태 갱신 함수
async function checkLoginStatus() {
    const buttons = document.querySelectorAll('[id^="favorite-btn-"]');

    if (!isLoggedIn) {
        buttons.forEach(button => button.disabled = true);
        return;
    }

    try {
        const res = await fetch('/scrap/list');
        if (!res.ok) throw new Error('스크랩 목록 응답 오류');
        const scrappedPropertyIds = await res.json(); // 서버에서 받은 property_id 목록

        buttons.forEach(button => {
            // 버튼 ID에서 숫자 형태의 propertyId 추출
            const propertyId = button.id.substring("favorite-btn-".length);
            button.disabled = false;

            // 서버에서 받은 ID 목록(숫자)과 버튼의 ID(문자열)를 비교하기 위해 형식을 통일
            if (scrappedPropertyIds.map(String).includes(propertyId)) {
                button.innerText = '찜 완료';
                button.classList.add('scrapped');
            } else {
                button.innerText = '찜하기';
                button.classList.remove('scrapped');
            }
        });
    } catch (err) {
        console.error('스크랩 목록 불러오기 실패:', err);
    }
}

// 찜하기 버튼 클릭 이벤트 처리 (추가/삭제 토글 기능)
async function addToFavorites(propertyId, event) {
    event.stopPropagation();
    const button = document.getElementById(`favorite-btn-${propertyId}`);

    if (!button) {
        alert('오류: 버튼을 찾을 수 없습니다.');
        return;
    }
    if (!isLoggedIn) {
        alert("로그인이 필요합니다. 로그인 페이지로 이동합니다.");
        return window.location.href = '/user/login';
    }

    button.disabled = true;
    button.innerText = '처리중...';

    const isScrapped = button.classList.contains('scrapped');

    try {
        if (isScrapped) {
            // --- 찜 삭제 처리 ---
            const response = await fetch(`/scrap/delete/${propertyId}`, {
                method: 'DELETE',
            });
            if (!response.ok) {
                const errorData = await response.json().catch(() => ({}));
                throw new Error(errorData.message || '찜 취소에 실패했습니다.');
            }
            alert("찜하기를 취소했습니다.");
            button.innerText = '찜하기';
            button.classList.remove('scrapped');
        } else {
            // --- 찜 추가 처리 ---
            // propertyId를 다시 생성하여 currentHousingList에서 원본 데이터 찾기
            const listItemData = currentHousingList.find(item => {
                const idToCompare = `${item.hsmpSn}${String(item.suplyPrvuseAr).replace('.', '')}${String(item.suplyCmnuseAr).replace('.', '')}`;
                return idToCompare === String(propertyId);
            });

            if (!listItemData) throw new Error("매물 정보를 찾을 수 없습니다.");

            const cleanValue = (value, fieldName) => {
                const numericFields = ['parkngCo', 'bassRentGtn', 'bassMtRntchrg', 'bassCnvrsGtnLmt', 'hshldCo', 'suplyPrvuseAr', 'suplyCmnuseAr'];
                if (value === undefined || value === null || (typeof value === 'object' && Object.keys(value).length === 0) || value === '') {
                    return numericFields.includes(fieldName) ? 0 : '정보없음';
                }
                return value;
            };

            const payload = {};
            for (const key in listItemData) {
                if (Object.prototype.hasOwnProperty.call(listItemData, key)) {
                    payload[key] = cleanValue(listItemData[key], key);
                }
            }
            // 테이블 구조에 맞게 property_id와 housing_info_id 추가
            payload.property_id = propertyId;
            payload.housing_info_id = listItemData.hsmpSn; // housing_info_id는 단지식별자로 지정
            payload.userId = userId;
            payload.isScrapped = true;

            const response = await fetch(`/scrap/add`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(payload)
            });

            if (!response.ok) {
                const errorData = await response.json().catch(() => ({}));
                throw new Error(errorData.message || '찜하기에 실패했습니다.');
            }

            alert("찜하기 완료!");
            button.innerText = '찜 완료';
            button.classList.add('scrapped');
        }
    } catch (error) {
        console.error('찜하기/취소 처리 실패:', error);
        alert(error.message || "오류가 발생했습니다.");
        button.innerText = isScrapped ? '찜 완료' : '찜하기';
    } finally {
        button.disabled = false;
    }
}


// 전역 변수
var latitude = 37.566826;
var longitude = 126.9786567;
var geocoder;
var map;
var markers = [];
let activeInfoWindow = null;
let currentHousingList = [];
let selectedHouseType = '';

// 전역 요소
const sidoSelect = document.getElementById('sido-select');
const sigunguSelect = document.getElementById('sigungu-select');
const modalWrapper = document.getElementById('modal-wrapper');
const modalBody = document.getElementById('modal-body');
const closeModalBtn = document.getElementById('close-modal-btn');
const listPanel = document.getElementById('list-panel');
const advancedFilterPanel = document.getElementById('advanced-filter-panel');
const infoAddrEl = document.getElementById('info-addr');
const infoCoordsEl = document.getElementById('info-coords');

const regionData = { "11": { name: "서울특별시", sigungu: { "110": "종로구", "140": "중구", "170": "용산구", "200": "성동구", "215": "광진구", "230": "동대문구", "260": "중랑구", "290": "성북구", "305": "강북구", "320": "도봉구", "350": "노원구", "380": "은평구", "410": "서대문구", "440": "마포구", "470": "양천구", "500": "강서구", "530": "구로구", "545": "금천구", "560": "영등포구", "590": "동작구", "620": "관악구", "650": "서초구", "680": "강남구", "710": "송파구", "740": "강동구" } }, "26": { name: "부산광역시", sigungu: { "110": "중구", "140": "서구", "170": "동구", "200": "영도구", "230": "부산진구", "260": "동래구", "290": "남구", "320": "북구", "350": "해운대구", "380": "사하구", "410": "금정구", "440": "강서구", "470": "연제구", "500": "수영구", "530": "사상구", "710": "기장군" } }, "27": { name: "대구광역시", sigungu: { "110": "중구", "140": "동구", "170": "서구", "200": "남구", "230": "북구", "260": "수성구", "290": "달서구", "710": "달성군" } }, "28": { name: "인천광역시", sigungu: { "110": "중구", "140": "동구", "177": "미추홀구", "185": "연수구", "200": "남동구", "237": "부평구", "245": "계양구", "260": "서구", "710": "강화군", "720": "옹진군" } }, "29": { name: "광주광역시", sigungu: { "110": "동구", "140": "서구", "155": "남구", "170": "북구", "200": "광산구" } }, "30": { name: "대전광역시", sigungu: { "110": "동구", "140": "중구", "170": "서구", "200": "유성구", "230": "대덕구" } }, "31": { name: "울산광역시", sigungu: { "110": "중구", "140": "남구", "170": "동구", "200": "북구", "710": "울주군" } }, "36": { name: "세종특별자치시", sigungu: { "110": "세종특별자치시" } }, "41": { name: "경기", sigungu: { "111": "수원시 장안구", "113": "수원시 권선구", "115": "수원시 팔달구", "117": "수원시 영통구", "131": "성남시 수정구", "133": "성남시 중원구", "135": "성남시 분당구", "150": "의정부시", "171": "안양시 만안구", "173": "안양시 동안구", "190": "부천시", "210": "광명시", "220": "평택시", "250": "동두천시", "271": "안산시 상록구", "273": "안산시 단원구", "281": "고양시 덕양구", "285": "고양시 일산동구", "287": "고양시 일산서구", "290": "과천시", "310": "구리시", "360": "남양주시", "370": "오산시", "390": "시흥시", "410": "군포시", "430": "의왕시", "450": "하남시", "461": "용인시 처인구", "463": "용인시 기흥구", "465": "용인시 수지구", "480": "파주시", "500": "이천시", "550": "안성시", "570": "김포시", "590": "화성시", "610": "광주시", "630": "양주시", "650": "포천시", "670": "여주시", "800": "연천군", "820": "가평군", "830": "양평군" } }, "51": { name: "강원특별자치도", sigungu: { "110": "춘천시", "130": "원주시", "150": "강릉시", "170": "동해시", "190": "태백시", "210": "속초시", "230": "삼척시", "720": "홍천군", "730": "횡성군", "750": "영월군", "760": "평창군", "770": "정선군", "780": "철원군", "790": "화천군", "800": "양구군", "810": "인제군", "820": "고성군", "830": "양양군" } }, "43": { name: "충북", sigungu: { "111": "청주시 상당구", "112": "청주시 서원구", "113": "청주시 흥덕구", "114": "청주시 청원구", "130": "충주시", "150": "제천시", "720": "보은군", "730": "옥천군", "740": "영동군", "745": "증평군", "750": "진천군", "760": "괴산군", "770": "음성군", "800": "단양군" } }, "44": { name: "충남", sigungu: { "131": "천안시 동남구", "133": "천안시 서북구", "150": "공주시", "180": "보령시", "200": "아산시", "210": "서산시", "230": "논산시", "250": "계룡시", "270": "당진시", "710": "금산군", "760": "부여군", "770": "서천군", "790": "청양군", "800": "홍성군", "810": "예산군", "825": "태안군" } }, "52": { name: "전북특별자치도", sigungu: { "111": "전주시 완산구", "113": "전주시 덕진구", "130": "군산시", "140": "익산시", "180": "정읍시", "190": "남원시", "210": "김제시", "710": "완주군", "720": "진안군", "730": "무주군", "740": "장수군", "750": "임실군", "770": "순창군", "790": "고창군", "800": "부안군" } }, "46": { name: "전남", sigungu: { "110": "목포시", "130": "여수시", "150": "순천시", "170": "나주시", "230": "광양시", "710": "담양군", "720": "곡성군", "730": "구례군", "770": "고흥군", "780": "보성군", "800": "화순군", "810": "장흥군", "820": "강진군", "830": "해남군", "840": "영암군", "860": "무안군", "870": "함평군", "880": "영광군", "890": "장성군", "900": "완도군", "910": "진도군" } }, "47": { name: "경북", sigungu: { "111": "포항시 남구", "113": "포항시 북구", "130": "경주시", "150": "김천시", "170": "안동시", "190": "구미시", "210": "영주시", "230": "영천시", "250": "상주시", "280": "문경시", "290": "경산시", "720": "군위군", "730": "의성군", "750": "청송군", "760": "영양군", "770": "영덕군", "820": "청도군", "830": "고령군", "840": "성주군", "850": "칠곡군", "900": "예천군", "920": "봉화군", "930": "울진군", "940": "울릉군" } }, "48": { name: "경남", sigungu: { "121": "창원시 의창구", "123": "창원시 성산구", "125": "창원시 마산합포구", "127": "창원시 마산회원구", "129": "창원시 진해구", "170": "진주시", "220": "통영시", "240": "사천시", "250": "김해시", "270": "밀양시", "310": "거제시", "330": "양산시", "720": "의령군", "730": "함안군", "740": "창녕군", "820": "고성군", "840": "남해군", "850": "하동군", "860": "산청군", "870": "함양군", "880": "거창군", "890": "합천군" } }, "50": { name: "제주특별자치도", sigungu: { "110": "제주시", "130": "서귀포시" } } };
const lhPhoneNumbers = { "서울": "02-3416-3600", "경기남부": "031-250-8380", "부산울산": "051-460-5401", "대구경북": "053-603-2640", "광주전남": "062-360-3114", "대전충남": "042-470-0117", "강원": "033-258-4400", "경남": "055-210-8680", "전북": "063-230-6100", "제주": "064-720-1000", "충북": "1600-1004 (통합 콜센터)" };

function closeModal() {
    modalWrapper.style.display = 'none';
}

function closeListPanel() {
    listPanel.style.display = 'none';
    if (map) map.relayout();
}

window.onload = function() {
    setupRegionSearch();
    initializeMapAndLocation();
    closeModalBtn.addEventListener('click', closeModal);
    modalWrapper.addEventListener('click', (e) => {
        if (e.target === modalWrapper) closeModal();
    });
    const closeListPanelBtn = document.getElementById('close-list-panel-btn');
    if (closeListPanelBtn) closeListPanelBtn.addEventListener('click', closeListPanel);
    listPanel.style.display = 'none';
    advancedFilterPanel.style.display = 'none';
};

function initializeMapAndLocation() {
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(
            pos => {
                latitude = pos.coords.latitude;
                longitude = pos.coords.longitude;
                drawMap();
            },
            () => {
                alert("현재 위치를 가져올 수 없어 기본 위치로 지도를 표시합니다.");
                drawMap();
            }
        );
    } else {
        alert("이 브라우저에서는 위치 정보 기능을 지원하지 않습니다.");
        drawMap();
    }
}

function drawMap() {
    const mapContainer = document.getElementById('map');
    const mapOption = { center: new kakao.maps.LatLng(latitude, longitude), level: 8 };
    map = new kakao.maps.Map(mapContainer, mapOption);
    geocoder = new kakao.maps.services.Geocoder();
    kakao.maps.event.addListener(map, 'idle', () => {
        const newCenter = map.getCenter();
        latitude = newCenter.getLat();
        longitude = newCenter.getLng();
        displayLocationInfo(latitude, longitude);
    });
    displayLocationInfo(latitude, longitude);
}

function displayLocationInfo(lat, lng) {
    infoAddrEl.innerText = "확인 중...";
    infoCoordsEl.innerText = `${lat.toFixed(6)}, ${lng.toFixed(6)}`;
    geocoder.coord2RegionCode(lng, lat, (result, status) => {
        if (status === kakao.maps.services.Status.OK && result.length > 0) {
            const bjdInfo = result.find(r => r.region_type === 'B');
            if (bjdInfo) {
                infoAddrEl.innerText = bjdInfo.address_name;
                const sidoCode = bjdInfo.code.substring(0, 2);
                const sigunguCode = bjdInfo.code.substring(2, 5);
                sidoSelect.value = sidoCode;
                updateSigunguSelect(sidoCode);
                sigunguSelect.value = sigunguCode;
            } else {
                infoAddrEl.innerText = "법정동 정보 없음";
            }
        } else {
            infoAddrEl.innerText = "위치 정보 확인 실패";
        }
    });
}

function setupRegionSearch() {
    const searchBtn = document.getElementById('search-btn');
    const toggleAdvancedBtn = document.getElementById('toggle-advanced-search-btn');
    const applyFilterBtn = document.getElementById('apply-filter-btn');
    const resetFilterBtn = document.getElementById('reset-filter-btn');
    const loadingAnimation = document.getElementById('loading-animation');
    const listContent = document.getElementById('list-content');
    const areaPresetButtons = document.querySelectorAll('.area-preset-btn');
    const typeFilterButtons = document.querySelectorAll('.type-filter-btn');

    for (const code in regionData) {
        sidoSelect.appendChild(new Option(regionData[code].name, code));
    }
    updateSigunguSelect(sidoSelect.value);

    sidoSelect.addEventListener('change', () => updateSigunguSelect(sidoSelect.value));
    toggleAdvancedBtn.addEventListener('click', () => {
        advancedFilterPanel.style.display = advancedFilterPanel.style.display === 'flex' ? 'none' : 'flex';
    });

    searchBtn.addEventListener('click', async () => {
        loadingAnimation.style.display = 'block';
        listContent.style.display = 'none';
        listPanel.style.display = 'flex';
        const numOfRows = document.getElementById('num-of-rows-select').value;
        const sidoCode = sidoSelect.value;
        const signguCode = sigunguSelect.value;
        try {
            const response = await fetch(`/api/housing?brtcCode=${sidoCode}&signguCode=${signguCode}&numOfRows=${numOfRows}`);
            const data = await response.json();
            clearMarkers();
            await displayHousingOnMap(data.hsmpList || []);
        } catch (error) {
            console.error('API 호출 중 오류:', error);
        } finally {
            loadingAnimation.style.display = 'none';
            listContent.style.display = 'block';
            if (map) map.relayout();
        }
    });

    areaPresetButtons.forEach(button => {
        button.addEventListener('click', function() {
            document.getElementById('min-area').value = this.dataset.min;
            document.getElementById('max-area').value = this.dataset.max;
            applyFilterBtn.click();
        });
    });

    typeFilterButtons.forEach(button => {
        button.addEventListener('click', function() {
            typeFilterButtons.forEach(btn => btn.classList.remove('active'));
            this.classList.add('active');
            selectedHouseType = this.dataset.type;
            applyFilterBtn.click();
        });
    });

    applyFilterBtn.addEventListener('click', () => {
        const filters = {
            minDeposit: (parseInt(document.getElementById('min-deposit').value, 10) || 0) * 10000,
            maxDeposit: (parseInt(document.getElementById('max-deposit').value, 10) || Infinity) * 10000,
            minRent: (parseInt(document.getElementById('min-rent').value, 10) || 0) * 10000,
            maxRent: (parseInt(document.getElementById('max-rent').value, 10) || Infinity) * 10000,
            minArea: parseFloat(document.getElementById('min-area').value) || 0,
            maxArea: parseFloat(document.getElementById('max-area').value) || Infinity,
        };
        const filteredList = currentHousingList.filter(item => {
            const isDepositInRange = Number(item.bassRentGtn) >= filters.minDeposit && Number(item.bassRentGtn) <= filters.maxDeposit;
            const isRentInRange = Number(item.bassMtRntchrg) >= filters.minRent && Number(item.bassMtRntchrg) <= filters.maxRent;
            const isAreaInRange = parseFloat(item.suplyPrvuseAr) >= filters.minArea && parseFloat(item.suplyPrvuseAr) <= filters.maxArea;
            let isTypeMatch = true;
            if (selectedHouseType) {
                const mainTypes = ['다가구주택', '다세대주택', '아파트', '오피스텔'];
                const houseType = typeof item.houseTyNm === 'string' ? item.houseTyNm : '';
                isTypeMatch = selectedHouseType === 'etc' ? (houseType && !mainTypes.includes(houseType)) : (houseType === selectedHouseType);
            }
            return isDepositInRange && isRentInRange && isAreaInRange && isTypeMatch;
        });
        clearMarkers();
        displayHousingOnMap(filteredList, false); // 필터링 시에는 지오코딩 생략
    });

    resetFilterBtn.addEventListener('click', () => {
        ['min-deposit', 'max-deposit', 'min-rent', 'max-rent', 'min-area', 'max-area']
            .forEach(id => document.getElementById(id).value = '');
        selectedHouseType = '';
        typeFilterButtons.forEach(btn => btn.classList.remove('active'));
        document.querySelector('.type-filter-btn[data-type=""]').classList.add('active');
        clearMarkers();
        displayHousingOnMap(currentHousingList, false); // 필터링 시에는 지오코딩 생략
    });
}

function updateSigunguSelect(sidoCode) {
    sigunguSelect.innerHTML = '';
    const sigunguList = regionData[sidoCode]?.sigungu || {};
    for (const code in sigunguList) {
        sigunguSelect.appendChild(new Option(sigunguList[code], code));
    }
}

function clearMarkers() {
    markers.forEach(marker => marker.setMap(null));
    markers = [];
    if (activeInfoWindow) {
        activeInfoWindow.close();
        activeInfoWindow = null;
    }
}

async function displayHousingOnMap(housingList, needsGeocoding = true) {
    const propertyList = document.getElementById('property-list');
    propertyList.innerHTML = '';

    if (!housingList || housingList.length === 0) {
        propertyList.innerHTML = '<li style="text-align: center; padding: 20px; color: #555;">검색 결과가 없습니다.</li>';
        if (needsGeocoding) currentHousingList = [];
        return;
    }

    let displayList = housingList;
    if (needsGeocoding) {
        const geocodePromises = housingList.map(item =>
            new Promise((resolve) => {
                if (!item.rnAdres) return resolve({ ...item, coords: null });
                geocoder.addressSearch(item.rnAdres, (result, status) => {
                    const coords = (status === kakao.maps.services.Status.OK && result.length > 0)
                        ? new kakao.maps.LatLng(result[0].y, result[0].x)
                        : null;
                    resolve({ ...item, coords });
                });
            })
        );
        displayList = await Promise.all(geocodePromises);
        currentHousingList = displayList; // 지오코딩 후 전역 리스트 업데이트
    }

    displayList.forEach(itemData => {
        if (!itemData) return;

        // ★★★ 고유 숫자 propertyId 생성 ★★★
        const propertyId = `${itemData.hsmpSn}${String(itemData.suplyPrvuseAr).replace('.', '')}${String(itemData.suplyCmnuseAr).replace('.', '')}`;

        const { coords } = itemData;
        let marker = null;
        if (coords) {
            marker = new kakao.maps.Marker({ map, position: coords, title: itemData.hsmpNm });
            markers.push(marker);
        }

        const listItem = document.createElement('li');
        listItem.className = 'property-item';
        listItem.id = `property-${propertyId}`;

        const formattedDeposit = Number(itemData.bassRentGtn).toLocaleString('ko-KR');
        const formattedRent = Number(itemData.bassMtRntchrg).toLocaleString('ko-KR');

        listItem.innerHTML = `
            <strong>${itemData.hsmpNm || '이름 정보없음'}</strong>
            <p>주소: ${itemData.rnAdres || '주소 정보없음'}</p>
            <p>유형: ${itemData.suplyTyNm || '정보없음'} / ${typeof itemData.houseTyNm === 'object' ? '정보없음' : itemData.houseTyNm}</p>
            <p>전용/공용면적: ${itemData.suplyPrvuseAr}㎡ / ${itemData.suplyCmnuseAr}㎡</p>
            <p class="price">보증금: ${formattedDeposit}원 / 월세: ${formattedRent}원</p>
            <button id="favorite-btn-${propertyId}" onclick="addToFavorites('${propertyId}', event)" disabled>찜하기</button>
        `;
        propertyList.appendChild(listItem);

        const handleItemClick = () => {
            document.querySelectorAll('.property-item').forEach(li => li.classList.remove('active-item'));
            listItem.classList.add('active-item');
            populateAndShowModal(itemData);
            if (marker) {
                map.panTo(coords);
                if (activeInfoWindow) activeInfoWindow.close();
                const infowindow = new kakao.maps.InfoWindow({
                    content: `<div style="padding:5px;width:260px;"><strong>${itemData.hsmpNm}</strong><br><small>${itemData.rnAdres}</small></div>`,
                    removable: true
                });
                infowindow.open(map, marker);
                activeInfoWindow = infowindow;
            }
        };

        listItem.addEventListener('click', handleItemClick);
        if (marker) {
            kakao.maps.event.addListener(marker, 'click', () => {
                listItem.scrollIntoView({ behavior: 'smooth', block: 'center' });
                handleItemClick();
            });
        }
    });

    console.log("매물 정보 표시 완료.");
    checkLoginStatus();
}

function populateAndShowModal(data) {
    const checkValue = (value) => (value === undefined || value === null || (typeof value === 'object' && Object.keys(value).length === 0) || value === '') ? '정보없음' : value;
    const formatNumber = (value) => isNaN(Number(value)) ? '정보없음' : Number(value).toLocaleString('ko-KR');
    const findPhoneNumber = (insttNm) => {
        if (!insttNm) return '1600-1004 (통합 콜센터)';
        for (const region in lhPhoneNumbers) {
            if (insttNm.includes(region)) return lhPhoneNumbers[region];
        }
        return '1600-1004 (통합 콜센터)';
    };

    const entrpsTel = findPhoneNumber(data.insttNm);
    modalBody.innerHTML = `
        <table>
            <tr><th>단지식별자</th><td>${checkValue(data.hsmpSn)}</td></tr>
            <tr><th>단지명</th><td>${checkValue(data.hsmpNm)}</td></tr>
            <tr><th>주소</th><td>${checkValue(data.rnAdres)}</td></tr>
            <tr><th>준공일자</th><td>${checkValue(data.competDe)}</td></tr>
            <tr><th>세대수</th><td>${checkValue(data.hshldCo)}</td></tr>
            <tr><th>공급유형</th><td>${checkValue(data.suplyTyNm)}</td></tr>
            <tr><th>전용면적</th><td>${checkValue(data.suplyPrvuseAr)} ㎡</td></tr>
            <tr><th>공용면적</th><td>${checkValue(data.suplyCmnuseAr)} ㎡</td></tr>
            <tr><th>주택유형</th><td>${checkValue(data.houseTyNm)}</td></tr>
            <tr><th>난방방식</th><td>${checkValue(data.heatMthdDetailNm)}</td></tr>
            <tr><th>건물형태</th><td>${checkValue(data.buldStleNm)}</td></tr>
            <tr><th>승강기</th><td>${checkValue(data.elvtrInstlAtNm)}</td></tr>
            <tr><th>주차수</th><td>${checkValue(data.parkngCo)}</td></tr>
            <tr><th>보증금</th><td>${formatNumber(data.bassRentGtn)} 원</td></tr>
            <tr><th>월임대료</th><td>${formatNumber(data.bassMtRntchrg)} 원</td></tr>
            <tr><th>전환보증금</th><td>${formatNumber(data.bassCnvrsGtnLmt)} 원</td></tr>
            <tr><th>관리 기관</th><td>${checkValue(data.insttNm)}</td></tr>
            <tr><th>연락처</th><td>${entrpsTel}</td></tr>
        </table>
    `;
    modalWrapper.style.display = 'flex';
}