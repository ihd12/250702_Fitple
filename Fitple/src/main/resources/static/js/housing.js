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
        const scrappedIds = await res.json();

        buttons.forEach(button => {
            // pnu는 propertyId로 사용될 고유 식별자입니다.
            const propertyId = button.closest('li.property-item').getAttribute('data-pnu');

            button.disabled = false; // 기본적으로 활성화

            if (scrappedIds.includes(propertyId)) {
                button.innerText = '찜 완료';
                button.classList.add('scrapped');
                button.disabled = true; // 이미 찜한 주거지 버튼은 비활성화
            } else {
                button.innerText = '찜하기';
                button.classList.remove('scrapped');
            }
        });
    } catch (err) {
        console.error('스크랩 목록 불러오기 실패:', err);
    }
}

// 찜하기 버튼 클릭 이벤트 처리
function addToFavorites(propertyId, event) {
    event.stopPropagation();  // 클릭 이벤트 전파 방지
    const button = document.getElementById(`favorite-btn-${propertyId}`);

    if (!isLoggedIn) {
        alert("로그인이 필요합니다. 로그인 페이지로 이동합니다.");
        window.location.href = '/user/login';
        return;
    }

    if (button.disabled) return;

    button.disabled = true;
    button.innerText = '처리중...';

    const listItem = button.closest('li.property-item');

    // dataset에서 추가 데이터 가져오기
    const hsmpSn = listItem.dataset.hsmpSn;
    const brtcCode = listItem.dataset.brtcCode;
    const signguCode = listItem.dataset.signguCode;
    const brtcNm = listItem.dataset.brtcNm;
    const signguNm = listItem.dataset.signguNm;
    const insttNm = listItem.dataset.insttNm; // 기관명
    const hsmpNm = listItem.dataset.hsmpNm; // 단지명
    const rnAdres = listItem.dataset.rnAdres; // 도로명 주소
    const competDe = listItem.dataset.competDe; // 준공 일자
    const hshldCo = listItem.dataset.hshldCo; // 세대수
    const suplyTyNm = listItem.dataset.suplyTyNm; // 공급 유형 명
    const styleNm = listItem.dataset.styleNm; // 형 명
    const suplyPrvuseAr = listItem.dataset.suplyPrvuseAr; // 공급 전용 면적
    const suplyCmnuseAr = listItem.dataset.suplyCmnuseAr; // 공급 공용 면적
    const houseTyNm = listItem.dataset.houseTyNm; // 주택 유형 명
    const heatMthdDetailNm = listItem.dataset.heatMthdDetailNm; // 난방 방식
    const buldStleNm = listItem.dataset.buldStleNm; // 건물 형태
    const elvtrInstlAtNm = listItem.dataset.elvtrInstlAtNm; // 승강기 설치여부
    const parkngCo = listItem.dataset.parkngCo; // 주차수
    const bassRentGtn = listItem.dataset.bassRentGtn; // 기본 임대보증금
    const bassMtRntchrg = listItem.dataset.bassMtRntchrg; // 기본 월임대료
    const bassCnvrsGtnLmt = listItem.dataset.bassCnvrsGtnLmt; // 기본 전환보증금
    const msg = listItem.dataset.msg; // 메시지

    // 로그인한 사용자 ID (전역 변수 userId 사용)
    fetch(`/scrap/add/${propertyId}`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
            hsmpSn,
            brtcCode,
            signguCode,
            brtcNm,
            signguNm,
            insttNm,
            hsmpNm,
            rnAdres,
            competDe,
            hshldCo,
            suplyTyNm,
            styleNm,
            suplyPrvuseAr,
            suplyCmnuseAr,
            houseTyNm,
            heatMthdDetailNm,
            buldStleNm,
            elvtrInstlAtNm,
            parkngCo,
            bassRentGtn,
            bassMtRntchrg,
            bassCnvrsGtnLmt,
            msg,
            housingInfoId: propertyId,
            userId, // 전역 변수 userId 사용
            isScrapped: true,
        })
    })
        .then(response => {
            if (!response.ok) throw new Error('Server error');
            return response.json();
        })
        .then(data => {
            alert("찜하기 완료!");
            button.innerText = '찜 완료';
            button.classList.add('scrapped');
            button.disabled = true;

            // 찜 상태 다시 갱신
            checkLoginStatus();
        })
        .catch(error => {
            console.error('Error:', error);
            alert("오류가 발생했습니다.");
            button.disabled = false;
            button.innerText = '찜하기';
        });
}


// 전역 변수
var latitude = 37.566826;
var longitude = 126.9786567;
var geocoder = new kakao.maps.services.Geocoder();
var map;
var markers = [];
let activeInfoWindow = null;
let currentHousingList = [];
let selectedHouseType = '';

// 전역 요소 (DOM 요소들을 여기에 미리 선언)
const sidoSelect = document.getElementById('sido-select');
const sigunguSelect = document.getElementById('sigungu-select');
const modalWrapper = document.getElementById('modal-wrapper');
const modalBody = document.getElementById('modal-body');
const closeModalBtn = document.getElementById('close-modal-btn');
const listPanel = document.getElementById('list-panel'); // 매물 리스트 패널
const advancedFilterPanel = document.getElementById('advanced-filter-panel'); // 상세 검색 패널
const infoAddrEl = document.getElementById('info-addr'); // 지도 아래에 새로 추가된 info-addr 요소
const infoCoordsEl = document.getElementById('info-coords'); // 지도 아래에 새로 추가된 info-coords 요소
const closeListPanelBtn = document.getElementById('close-list-panel-btn'); // 새로 추가된 닫기 버튼 요소


const regionData = { "11": { name: "서울특별시", sigungu: { "110": "종로구", "140": "중구", "170": "용산구", "200": "성동구", "215": "광진구", "230": "동대문구", "260": "중랑구", "290": "성북구", "305": "강북구", "320": "도봉구", "350": "노원구", "380": "은평구", "410": "서대문구", "440": "마포구", "470": "양천구", "500": "강서구", "530": "구로구", "545": "금천구", "560": "영등포구", "590": "동작구", "620": "관악구", "650": "서초구", "680": "강남구", "710": "송파구", "740": "강동구" } }, "26": { name: "부산광역시", sigungu: { "110": "중구", "140": "서구", "170": "동구", "200": "영도구", "230": "부산진구", "260": "동래구", "290": "남구", "320": "북구", "350": "해운대구", "380": "사하구", "410": "금정구", "440": "강서구", "470": "연제구", "500": "수영구", "530": "사상구", "710": "기장군" } }, "27": { name: "대구광역시", sigungu: { "110": "중구", "140": "동구", "170": "서구", "200": "남구", "230": "북구", "260": "수성구", "290": "달서구", "710": "달성군" } }, "28": { name: "인천광역시", sigungu: { "110": "중구", "140": "동구", "177": "미추홀구", "185": "연수구", "200": "남동구", "237": "부평구", "245": "계양구", "260": "서구", "710": "강화군", "720": "옹진군" } }, "29": { name: "광주광역시", sigungu: { "110": "동구", "140": "서구", "155": "남구", "170": "북구", "200": "광산구" } }, "30": { name: "대전광역시", sigungu: { "110": "동구", "140": "중구", "170": "서구", "200": "유성구", "230": "대덕구" } }, "31": { name: "울산광역시", sigungu: { "110": "중구", "140": "남구", "170": "동구", "200": "북구", "710": "울주군" } }, "36": { name: "세종특별자치시", sigungu: { "110": "세종특별자치시" } }, "41": { name: "경기", sigungu: { "111": "수원시 장안구", "113": "수원시 권선구", "115": "수원시 팔달구", "117": "수원시 영통구", "131": "성남시 수정구", "133": "성남시 중원구", "135": "성남시 분당구", "150": "의정부시", "171": "안양시 만안구", "173": "안양시 동안구", "190": "부천시", "210": "광명시", "220": "평택시", "250": "동두천시", "271": "안산시 상록구", "273": "안산시 단원구", "281": "고양시 덕양구", "285": "고양시 일산동구", "287": "고양시 일산서구", "290": "과천시", "310": "구리시", "360": "남양주시", "370": "오산시", "390": "시흥시", "410": "군포시", "430": "의왕시", "450": "하남시", "461": "용인시 처인구", "463": "용인시 기흥구", "465": "용인시 수지구", "480": "파주시", "500": "이천시", "550": "안성시", "570": "김포시", "590": "화성시", "610": "광주시", "630": "양주시", "650": "포천시", "670": "여주시", "800": "연천군", "820": "가평군", "830": "양평군" } }, "51": { name: "강원특별자치도", sigungu: { "110": "춘천시", "130": "원주시", "150": "강릉시", "170": "동해시", "190": "태백시", "210": "속초시", "230": "삼척시", "720": "홍천군", "730": "횡성군", "750": "영월군", "760": "평창군", "770": "정선군", "780": "철원군", "790": "화천군", "800": "양구군", "810": "인제군", "820": "고성군", "830": "양양군" } }, "43": { name: "충북", sigungu: { "111": "청주시 상당구", "112": "청주시 서원구", "113": "청주시 흥덕구", "114": "청주시 청원구", "130": "충주시", "150": "제천시", "720": "보은군", "730": "옥천군", "740": "영동군", "745": "증평군", "750": "진천군", "760": "괴산군", "770": "음성군", "800": "단양군" } }, "44": { name: "충남", sigungu: { "131": "천안시 동남구", "133": "천안시 서북구", "150": "공주시", "180": "보령시", "200": "아산시", "210": "서산시", "230": "논산시", "250": "계룡시", "270": "당진시", "710": "금산군", "760": "부여군", "770": "서천군", "790": "청양군", "800": "홍성군", "810": "예산군", "825": "태안군" } }, "52": { name: "전북특별자치도", sigungu: { "111": "전주시 완산구", "113": "전주시 덕진구", "130": "군산시", "140": "익산시", "180": "정읍시", "190": "남원시", "210": "김제시", "710": "완주군", "720": "진안군", "730": "무주군", "740": "장수군", "750": "임실군", "770": "순창군", "790": "고창군", "800": "부안군" } }, "46": { name: "전남", sigungu: { "110": "목포시", "130": "여수시", "150": "순천시", "170": "나주시", "230": "광양시", "710": "담양군", "720": "곡성군", "730": "구례군", "770": "고흥군", "780": "보성군", "800": "화순군", "810": "장흥군", "820": "강진군", "830": "해남군", "840": "영암군", "860": "무안군", "870": "함평군", "880": "영광군", "890": "장성군", "900": "완도군", "910": "진도군" } }, "47": { name: "경북", sigungu: { "111": "포항시 남구", "113": "포항시 북구", "130": "경주시", "150": "김천시", "170": "안동시", "190": "구미시", "210": "영주시", "230": "영천시", "250": "상주시", "280": "문경시", "290": "경산시", "720": "군위군", "730": "의성군", "750": "청송군", "760": "영양군", "770": "영덕군", "820": "청도군", "830": "고령군", "840": "성주군", "850": "칠곡군", "900": "예천군", "920": "봉화군", "930": "울진군", "940": "울릉군" } }, "48": { name: "경남", sigungu: { "121": "창원시 의창구", "123": "창원시 성산구", "125": "창원시 마산합포구", "127": "창원시 마산회원구", "129": "창원시 진해구", "170": "진주시", "220": "통영시", "240": "사천시", "250": "김해시", "270": "밀양시", "310": "거제시", "330": "양산시", "720": "의령군", "730": "함안군", "740": "창녕군", "820": "고성군", "840": "남해군", "850": "하동군", "860": "산청군", "870": "함양군", "880": "거창군", "890": "합천군" } }, "50": { name: "제주특별자치도", sigungu: { "110": "제주시", "130": "서귀포시" } } };
const lhPhoneNumbers = { "서울": "02-3416-3600", "경기남부": "031-250-8380", "부산울산": "051-460-5401", "대구경북": "053-603-2640", "광주전남": "062-360-3114", "대전충남": "042-470-0117", "강원": "033-258-4400", "경남": "055-210-8680", "전북": "063-230-6100", "제주": "064-720-1000", "충북": "1600-1004 (통합 콜센터)" };

// closeModal 함수 정의
function closeModal() {
    modalWrapper.style.display = 'none';
}

// closeListPanel 함수 정의 (매물 리스트 패널 닫기)
function closeListPanel() {
    listPanel.style.display = 'none';
    // 매물 리스트가 닫힐 때 지도 크기 조절 (선택 사항)
    if (map) { // 맵 객체가 초기화되었는지 확인
        map.relayout();
    }
}


window.onload = function() {
    setupRegionSearch();
    initializeMapAndLocation();

    closeModalBtn.addEventListener('click', closeModal);
    modalWrapper.addEventListener('click', (e) => {
        if (e.target === modalWrapper) {
            closeModal();
        }
    });

    // 새로 추가된 매물 리스트 닫기 버튼 이벤트 리스너
    if (closeListPanelBtn) { // 버튼이 HTML에 존재하는지 확인
        closeListPanelBtn.addEventListener('click', closeListPanel);
    }


    // 페이지 로드 시 매물 리스트 패널 숨기기 (기존대로 유지)
    listPanel.style.display = 'none';
    // 상세 검색 패널도 초기에는 숨김 (기존대로 유지)
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
            error => {
                console.error("Geolocation 에러:", error.message);
                alert("현재 위치를 가져올 수 없습니다. 기본 위치로 지도를 표시합니다.");
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

    kakao.maps.event.addListener(map, 'tilesloaded', function(){
        displayLocationInfo(latitude, longitude);
    });

    kakao.maps.event.addListener(map, 'idle', function() {
        const newCenter = map.getCenter();
        if(newCenter.getLat() !== latitude || newCenter.getLng() !== longitude){
            latitude = newCenter.getLat();
            longitude = newCenter.getLng();
            displayLocationInfo(latitude, longitude);
        }
    });
}

function displayLocationInfo(lat, lng) {
    // Correctly reference the new elements for address and coordinates
    const infoAddrEl = document.getElementById('info-addr');
    const infoCoordsEl = document.getElementById('info-coords');

    infoAddrEl.innerText = "확인 중...";
    infoCoordsEl.innerText = "확인 중...";

    geocoder.coord2RegionCode(lng, lat, (result, status) => {
        if (status === kakao.maps.services.Status.OK) {
            const bjdInfo = result.find(r => r.region_type === 'B');
            if (bjdInfo) {
                infoAddrEl.innerText = bjdInfo.address_name;
                infoCoordsEl.innerText = `${lat.toFixed(6)}, ${lng.toFixed(6)}`;
                const sidoCode = bjdInfo.code.substring(0, 2);
                const sigunguCode = bjdInfo.code.substring(2, 5);
                sidoSelect.value = sidoCode;
                updateSigunguSelect(sidoCode);
                sigunguSelect.value = sigunguCode;
            } else {
                infoAddrEl.innerText = "법정동 정보 없음";
                infoCoordsEl.innerText = `${lat.toFixed(6)}, ${lng.toFixed(6)}`;
            }
        } else {
            infoAddrEl.innerText = "위치 정보 확인 실패";
            infoCoordsEl.innerText = "위치 정보 확인 실패";
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
    const minAreaInput = document.getElementById('min-area');
    const maxAreaInput = document.getElementById('max-area');
    const typeFilterButtons = document.querySelectorAll('.type-filter-btn');

    for (const code in regionData) {
        const option = document.createElement('option');
        option.value = code;
        option.innerText = regionData[code].name;
        sidoSelect.appendChild(option);
    }
    sidoSelect.addEventListener('change', () => { updateSigunguSelect(sidoSelect.value); });
    sigunguSelect.addEventListener('change', () => {});

    toggleAdvancedBtn.addEventListener('click', () => {
        const isVisible = advancedFilterPanel.style.display === 'flex';
        advancedFilterPanel.style.display = isVisible ? 'none' : 'flex';
    });

    searchBtn.addEventListener('click', () => {
        loadingAnimation.style.display = 'block';
        listContent.style.display = 'none';
        listPanel.style.display = 'flex'; // Display the list panel

        const numOfRows = document.getElementById('num-of-rows-select').value;
        const sidoCode = sidoSelect.value;
        const signguCode = sigunguSelect.value;

        fetch(`/api/housing?brtcCode=${sidoCode}&signguCode=${signguCode}&numOfRows=${numOfRows}`).then(response => response.json()).then(data => {
            currentHousingList = data.hsmpList || [];
            clearMarkers();
            displayHousingOnMap(currentHousingList);
        }).catch(error => console.error('API 호출 중 오류:', error)).finally(() => {
            loadingAnimation.style.display = 'none';
            listContent.style.display = 'block';
            if (map) { // Recalculate map size after list panel appears
                map.relayout();
            }
        });
    });

    areaPresetButtons.forEach(button => {
        button.addEventListener('click', function() {
            minAreaInput.value = this.dataset.min;
            maxAreaInput.value = this.dataset.max;
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
        const minDeposit = document.getElementById('min-deposit').value;
        const maxDeposit = document.getElementById('max-deposit').value;
        const minRent = document.getElementById('min-rent').value;
        const maxRent = document.getElementById('max-rent').value;
        const minArea = document.getElementById('min-area').value;
        const maxArea = document.getElementById('max-area').value;

        const minDepositValue = minDeposit ? parseInt(minDeposit) * 10000 : 0;
        const maxDepositValue = maxDeposit ? parseInt(maxDeposit) * 10000 : Infinity;
        const minRentValue = minRent ? parseInt(minRent) * 10000 : 0;
        const maxRentValue = maxRent ? parseInt(maxRent) * 10000 : Infinity;
        const minAreaValue = minArea ? parseFloat(minArea) : 0;
        const maxAreaValue = maxArea ? parseFloat(maxArea) : Infinity;

        const filteredList = currentHousingList.filter(item => {
            const deposit = Number(item.bassRentGtn);
            const rent = Number(item.bassMtRntchrg);
            const area = parseFloat(item.suplyPrvuseAr);
            const houseType = typeof item.houseTyNm === 'string' ? item.houseTyNm : '';

            const isDepositInRange = deposit >= minDepositValue && deposit <= maxDepositValue;
            const isRentInRange = rent >= minRentValue && rent <= maxRentValue;
            const isAreaInRange = area >= minAreaValue && area <= maxAreaValue;

            let isTypeMatch = true;
            if (selectedHouseType) {
                const mainTypes = ['다가구주택', '다세대주택', '아파트', '오피스텔'];
                if (selectedHouseType === 'etc') {
                    isTypeMatch = houseType && !mainTypes.includes(houseType);
                } else {
                    isTypeMatch = houseType === selectedHouseType;
                }
            }
            return isDepositInRange && isRentInRange && isAreaInRange && isTypeMatch;
        });
        clearMarkers();
        displayHousingOnMap(filteredList);
    });

    resetFilterBtn.addEventListener('click', () => {
        document.getElementById('min-deposit').value = '';
        document.getElementById('max-deposit').value = '';
        document.getElementById('min-rent').value = '';
        document.getElementById('max-rent').value = '';
        document.getElementById('min-area').value = '';
        document.getElementById('max-area').value = '';

        selectedHouseType = '';
        typeFilterButtons.forEach(btn => btn.classList.remove('active'));
        document.querySelector('.type-filter-btn[data-type=""]').classList.add('active');

        clearMarkers();
        displayHousingOnMap(currentHousingList);
    });

    updateSigunguSelect(sidoSelect.value);
}

function updateSigunguSelect(sidoCode) {
    sigunguSelect.innerHTML = '';
    const sigunguList = regionData[sidoCode]?.sigungu || {};
    for (const code in sigunguList) {
        const option = document.createElement('option');
        option.value = code;
        option.innerText = sigunguList[code];
        sigunguSelect.appendChild(option);
    }
    // 시/군/구 선택 시 자동으로 첫 번째 옵션 선택 (선택지가 없으면 기본 상태 유지)
    if (sigunguSelect.options.length > 0) {
        sigunguSelect.value = sigunguSelect.options[0].value;
    }
}

function clearMarkers() {
    for (var i = 0; i < markers.length; i++) {
        markers[i].setMap(null);
    }
    markers = [];
    if (activeInfoWindow) {
        activeInfoWindow.close();
        activeInfoWindow = null;
    }
}

// 매물 리스트 + 지도 + 찜버튼 추가 렌더링
async function displayHousingOnMap(housingList) {
    const propertyList = document.getElementById('property-list');
    propertyList.innerHTML = '';  // 기존 내용 삭제

    if (!housingList || housingList.length === 0) {
        propertyList.innerHTML = '<li style="text-align: center; padding: 20px; color: #555;">검색 결과가 없습니다.</li>';
        return;
    }

    // 주소 검색을 위한 Promises 배열 생성
    const geocodePromises = housingList.map(item => {
        return new Promise((resolve) => {
            // item.rnAdres가 없을 경우 처리 (필요시 item.rnAdres로 변경)
            if (!item.rnAdres) {
                console.warn("rnAdres 값이 없어 지오코딩을 건너뜁니다:", item);
                resolve(null); // 주소 없으면 null 반환
                return;
            }
            geocoder.addressSearch(item.rnAdres, (result, status) => {
                if (status === kakao.maps.services.Status.OK && result.length > 0) {
                    // Use `result[0].address.address_name` as pnu for consistent ID
                    resolve({ ...item, coords: new kakao.maps.LatLng(result[0].y, result[0].x), pnu: result[0].address.address_name });
                } else {
                    console.warn(`주소 검색 실패: ${item.rnAdres}, 상태: ${status}`);
                    resolve(null); // 검색 실패시 null 반환
                }
            });
        });
    });

    // 모든 주소 검색이 완료될 때까지 기다림
    const geocodedResults = await Promise.all(geocodePromises);

    for (const geocodeResult of geocodedResults) {
        if (geocodeResult) {
            const { coords, ...itemData } = geocodeResult;
            let marker = null;
            let infowindow = null;

            if (coords) { // Only create marker if coordinates are available
                marker = new kakao.maps.Marker({ map: map, position: coords, title: itemData.hsmpNm });
                const iwContent = `<div style="padding:5px;width:260px;"><strong>${itemData.hsmpNm}</strong><br><small>${itemData.rnAdres}</small></div>`;
                infowindow = new kakao.maps.InfoWindow({ content: iwContent, removable: true });
                markers.push(marker);
            }

            const listItem = document.createElement('li');
            listItem.className = 'property-item';
            // Use itemData.pnu as ID and data-pnu. Ensure itemData.pnu is robust.
            listItem.id = `property-${itemData.pnu}`;
            listItem.dataset.pnu = itemData.pnu;


            // dataset for extra data (unchanged)
            listItem.dataset.hsmpSn = itemData.hsmpSn;
            listItem.dataset.brtcCode = itemData.brtcCode;
            listItem.dataset.signguCode = itemData.signguCode;
            listItem.dataset.brtcNm = itemData.brtcNm;
            listItem.dataset.signguNm = itemData.signguNm;
            listItem.dataset.insttNm = itemData.insttNm;
            listItem.dataset.hsmpNm = itemData.hsmpNm;
            listItem.dataset.rnAdres = itemData.rnAdres;
            listItem.dataset.competDe = itemData.competDe;
            listItem.dataset.hshldCo = itemData.hshldCo;
            listItem.dataset.suplyTyNm = itemData.suplyTyNm;
            listItem.dataset.styleNm = itemData.styleNm;
            listItem.dataset.suplyPrvuseAr = itemData.suplyPrvuseAr;
            listItem.dataset.suplyCmnuseAr = itemData.suplyCmnuseAr;
            listItem.dataset.houseTyNm = itemData.houseTyNm;
            listItem.dataset.heatMthdDetailNm = itemData.heatMthdDetailNm;
            listItem.dataset.buldStleNm = itemData.buldStleNm;
            listItem.dataset.elvtrInstlAtNm = itemData.elvtrInstlAtNm;
            listItem.dataset.parkngCo = itemData.parkngCo;
            listItem.dataset.bassRentGtn = itemData.bassRentGtn;
            listItem.dataset.bassMtRntchrg = itemData.bassMtRntchrg;
            listItem.dataset.bassCnvrsGtnLmt = itemData.bassCnvrsGtnLmt;
            listItem.dataset.msg = itemData.msg;

            const formattedDeposit = Number(itemData.bassRentGtn).toLocaleString('ko-KR');
            const formattedRent = Number(itemData.bassMtRntchrg).toLocaleString('ko-KR');

            listItem.innerHTML = `
                <strong>${itemData.hsmpNm}</strong>
                <p>단지식별자: ${itemData.hsmpSn}</p>
                <p>주소: ${itemData.rnAdres}</p>
                <p>유형: ${itemData.suplyTyNm || '정보없음'} / ${typeof itemData.houseTyNm === 'object' ? '정보없음' : itemData.houseTyNm}</p>
                <p>전용면적: ${itemData.suplyPrvuseAr} ㎡</p>
                <p class="price">보증금: ${formattedDeposit}원 / 월세: ${formattedRent}원</p>
                <p>기관: <span class="instt-button">${itemData.insttNm}</span></p>
                <button id="favorite-btn-${itemData.pnu}" onclick="addToFavorites('${itemData.pnu}', event)" disabled>찜하기</button>
            `;

            propertyList.appendChild(listItem);

            // Event listeners
            if (coords && marker && infowindow) { // Attach map-related events only if marker exists
                const openInfoWindow = () => {
                    if (activeInfoWindow) { activeInfoWindow.close(); }
                    infowindow.open(map, marker);
                    activeInfoWindow = infowindow;
                };

                listItem.addEventListener('click', () => {
                    openInfoWindow();
                    map.panTo(coords);
                    document.querySelectorAll('.property-item').forEach(li => li.classList.remove('active-item'));
                    listItem.classList.add('active-item');
                    // Pass itemData directly
                    populateAndShowModal(itemData); // 여기를 itemData로 수정했음
                });

                kakao.maps.event.addListener(marker, 'click', () => {
                    openInfoWindow();
                    listItem.scrollIntoView({ behavior: 'smooth', block: 'start' });
                    document.querySelectorAll('.property-item').forEach(li => li.classList.remove('active-item'));
                    listItem.classList.add('active-item');
                    // Pass itemData directly
                    populateAndShowModal(itemData); // 여기를 itemData로 수정했음
                });
            } else {
                // If no marker (geocoding failed), still allow modal to open from list item click
                listItem.addEventListener('click', () => {
                    document.querySelectorAll('.property-item').forEach(li => li.classList.remove('active-item'));
                    listItem.classList.add('active-item');
                    // Pass itemData directly
                    populateAndShowModal(itemData); // 여기를 itemData로 수정했음
                });
            }
        }
        // Small delay if needed for large number of items (consider if it slows down too much)
        // await new Promise(resolve => setTimeout(resolve, 50));
    }

    console.log("매물 정보 표시 완료.");
    checkLoginStatus();
}

// populateAndShowModal function now directly uses the 'data' object passed to it.
function populateAndShowModal(data) { // Parameter name is 'data' (represents the itemData passed)
    // Removed the currentHousingList.find() call and the associated 'alert' and 'return'
    // as 'data' is now directly the itemData object.

    const checkValue = (value) => {
        if (value === undefined || value === null || (typeof value === 'object' && Object.keys(value).length === 0) || value === '') return '정보없음';
        return value;
    };
    const formatNumber = (value) => {
        const num = Number(value);
        if (isNaN(num)) return '정보없음';
        return num.toLocaleString('ko-KR');
    };
    const findPhoneNumber = (insttNm) => {
        if (!insttNm) return '1600-1004 (통합 콜센터)';
        for (const region in lhPhoneNumbers) {
            if (insttNm.includes(region)) {
                return lhPhoneNumbers[region];
            }
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