const isLoggedIn = document.body.dataset.isAuthenticated === 'true';
const userId = document.body.dataset.userId;

function getUserId() {
    return userId === 'null' || userId === null ? null : userId;
}

// 찜하기 버튼 상태 갱신 함수
async function checkLoginStatus() {
    const buttons = document.querySelectorAll('[id^="favorite-btn-"]');

    if (!isLoggedIn) {
        buttons.forEach(button => button.disabled = true);
        return;
    }

    try {
        const res = await fetch('/scrap/list');
        const scrappedData = await res.json();

        scrappedData.forEach(item => {
            const hsmpSn = item.hsmpSn;
            const area = item.suplyPrvuseAr;
            const btnId = `favorite-btn-${hsmpSn}-${area}`;

            const button = document.getElementById(btnId);
            if (!button) return;

            const listItem = button.closest('li.property-item');
            if (!listItem) return;

            button.innerText = '찜 완료';
            button.classList.add('scrapped');

            const existingBadge = listItem.querySelector('.scrap-badge');
            if (!existingBadge) {
                const badge = document.createElement('span');
                badge.className = 'badge bg-warning text-dark scrap-badge';
                badge.innerText = '★ 저장';
                const infoBox = listItem.querySelector('.property-info');
                if (infoBox) infoBox.appendChild(badge);
            }
        });

    } catch (err) {
        console.error('스크랩 목록 불러오기 실패:', err);
    }
}




// 찜하기 버튼 클릭 이벤트 처리
function addToFavorites(propertyId, event) {
    event.stopPropagation();
    const button = event.currentTarget;

    if (!isLoggedIn) {
        alert("로그인이 필요합니다. 로그인 페이지로 이동합니다.");
        window.location.href = '/user/login';
        return;
    }

    const listItem = button.closest('li.property-item');

    const hsmpSn = listItem.dataset.hsmpSn;
    const brtcCode = listItem.dataset.brtcCode;
    const signguCode = listItem.dataset.signguCode;
    const brtcNm = listItem.dataset.brtcNm;
    const signguNm = listItem.dataset.signguNm;
    const insttNm = listItem.dataset.insttNm;
    const hsmpNm = listItem.dataset.hsmpNm;
    const rnAdres = listItem.dataset.rnAdres;
    const competDe = listItem.dataset.competDe;
    const hshldCo = listItem.dataset.hshldCo;
    const suplyTyNm = listItem.dataset.suplyTyNm;
    const styleNm = listItem.dataset.styleNm;
    const suplyPrvuseAr = listItem.dataset.suplyPrvuseAr;
    const suplyCmnuseAr = listItem.dataset.suplyCmnuseAr;
    const houseTyNm = listItem.dataset.houseTyNm;
    const heatMthdDetailNm = listItem.dataset.heatMthdDetailNm;
    const buldStleNm = listItem.dataset.buldStleNm;
    const elvtrInstlAtNm = listItem.dataset.elvtrInstlAtNm;
    const parkngCo = listItem.dataset.parkngCo;
    const bassRentGtn = listItem.dataset.bassRentGtn;
    const bassMtRntchrg = listItem.dataset.bassMtRntchrg;
    const bassCnvrsGtnLmt = listItem.dataset.bassCnvrsGtnLmt;
    const msg = listItem.dataset.msg;

    const userId = getUserId();

    if (!button.classList.contains('scrapped')) {
        const scrapData = {
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
            isScrapped: true
        };

        button.innerText = '처리중...';

        fetch('/scrap/add', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(scrapData)
        })
            .then(res => res.ok ? res.json() : Promise.reject("추가 실패"))
            .then(() => {
                button.innerText = "찜 완료";
                button.classList.add("scrapped");
                checkLoginStatus();
            })
            .catch(err => {
                console.error("찜 추가 오류:", err);
                button.innerText = "찜하기";
            });
    } else {
        // 찜취소 요청
        fetch(`/scrap/delete?hsmpSn=${hsmpSn}&area=${area}`, {
            method: 'DELETE'
        })
            .then(res => res.ok ? res.text() : Promise.reject("삭제 실패"))
            .then(() => {
                button.innerText = "찜하기";
                button.classList.remove("scrapped");

                const badge = button.closest('li.property-item')?.querySelector('.scrap-badge');
                if (badge) badge.remove();

                checkLoginStatus();
            })
            .catch(err => {
                console.error("찜 취소 오류:", err);
            });
    }
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

// 전역 요소
const sidoSelect = document.getElementById('sido-select');
const sigunguSelect = document.getElementById('sigungu-select');
const modalWrapper = document.getElementById('modal-wrapper');
const modalBody = document.getElementById('modal-body');
const closeModalBtn = document.getElementById('close-modal-btn');
const regionData = { "11": { name: "서울특별시", sigungu: { "110": "종로구", "140": "중구", "170": "용산구", "200": "성동구", "215": "광진구", "230": "동대문구", "260": "중랑구", "290": "성북구", "305": "강북구", "320": "도봉구", "350": "노원구", "380": "은평구", "410": "서대문구", "440": "마포구", "470": "양천구", "500": "강서구", "530": "구로구", "545": "금천구", "560": "영등포구", "590": "동작구", "620": "관악구", "650": "서초구", "680": "강남구", "710": "송파구", "740": "강동구" } }, "26": { name: "부산광역시", sigungu: { "110": "중구", "140": "서구", "170": "동구", "200": "영도구", "230": "부산진구", "260": "동래구", "290": "남구", "320": "북구", "350": "해운대구", "380": "사하구", "410": "금정구", "440": "강서구", "470": "연제구", "500": "수영구", "530": "사상구", "710": "기장군" } }, "27": { name: "대구광역시", sigungu: { "110": "중구", "140": "동구", "170": "서구", "200": "남구", "230": "북구", "260": "수성구", "290": "달서구", "710": "달성군" } }, "28": { name: "인천광역시", sigungu: { "110": "중구", "140": "동구", "177": "미추홀구", "185": "연수구", "200": "남동구", "237": "부평구", "245": "계양구", "260": "서구", "710": "강화군", "720": "옹진군" } }, "29": { name: "광주광역시", sigungu: { "110": "동구", "140": "서구", "155": "남구", "170": "북구", "200": "광산구" } }, "30": { name: "대전광역시", sigungu: { "110": "동구", "140": "중구", "170": "서구", "200": "유성구", "230": "대덕구" } }, "31": { name: "울산광역시", sigungu: { "110": "중구", "140": "남구", "170": "동구", "200": "북구", "710": "울주군" } }, "36": { name: "세종특별자치시", sigungu: { "110": "세종특별자치시" } }, "41": { name: "경기", sigungu: { "111": "수원시 장안구", "113": "수원시 권선구", "115": "수원시 팔달구", "117": "수원시 영통구", "131": "성남시 수정구", "133": "성남시 중원구", "135": "성남시 분당구", "150": "의정부시", "171": "안양시 만안구", "173": "안양시 동안구", "190": "부천시", "210": "광명시", "220": "평택시", "250": "동두천시", "271": "안산시 상록구", "273": "안산시 단원구", "281": "고양시 덕양구", "285": "고양시 일산동구", "287": "고양시 일산서구", "290": "과천시", "310": "구리시", "360": "남양주시", "370": "오산시", "390": "시흥시", "410": "군포시", "430": "의왕시", "450": "하남시", "461": "용인시 처인구", "463": "용인시 기흥구", "465": "용인시 수지구", "480": "파주시", "500": "이천시", "550": "안성시", "570": "김포시", "590": "화성시", "610": "광주시", "630": "양주시", "650": "포천시", "670": "여주시", "800": "연천군", "820": "가평군", "830": "양평군" } }, "51": { name: "강원특별자치도", sigungu: { "110": "춘천시", "130": "원주시", "150": "강릉시", "170": "동해시", "190": "태백시", "210": "속초시", "230": "삼척시", "720": "홍천군", "730": "횡성군", "750": "영월군", "760": "평창군", "770": "정선군", "780": "철원군", "790": "화천군", "800": "양구군", "810": "인제군", "820": "고성군", "830": "양양군" } }, "43": { name: "충북", sigungu: { "111": "청주시 상당구", "112": "청주시 서원구", "113": "청주시 흥덕구", "114": "청주시 청원구", "130": "충주시", "150": "제천시", "720": "보은군", "730": "옥천군", "740": "영동군", "745": "증평군", "750": "진천군", "760": "괴산군", "770": "음성군", "800": "단양군" } }, "44": { name: "충남", sigungu: { "131": "천안시 동남구", "133": "천안시 서북구", "150": "공주시", "180": "보령시", "200": "아산시", "210": "서산시", "230": "논산시", "250": "계룡시", "270": "당진시", "710": "금산군", "760": "부여군", "770": "서천군", "790": "청양군", "800": "홍성군", "810": "예산군", "825": "태안군" } }, "52": { name: "전북특별자치도", sigungu: { "111": "전주시 완산구", "113": "전주시 덕진구", "130": "군산시", "140": "익산시", "180": "정읍시", "190": "남원시", "210": "김제시", "710": "완주군", "720": "진안군", "730": "무주군", "740": "장수군", "750": "임실군", "770": "순창군", "790": "고창군", "800": "부안군" } }, "46": { name: "전남", sigungu: { "110": "목포시", "130": "여수시", "150": "순천시", "170": "나주시", "230": "광양시", "710": "담양군", "720": "곡성군", "730": "구례군", "770": "고흥군", "780": "보성군", "800": "화순군", "810": "장흥군", "820": "강진군", "830": "해남군", "840": "영암군", "860": "무안군", "870": "함평군", "880": "영광군", "890": "장성군", "900": "완도군", "910": "진도군" } }, "47": { name: "경북", sigungu: { "111": "포항시 남구", "113": "포항시 북구", "130": "경주시", "150": "김천시", "170": "안동시", "190": "구미시", "210": "영주시", "230": "영천시", "250": "상주시", "280": "문경시", "290": "경산시", "720": "군위군", "730": "의성군", "750": "청송군", "760": "영양군", "770": "영덕군", "820": "청도군", "830": "고령군", "840": "성주군", "850": "칠곡군", "900": "예천군", "920": "봉화군", "930": "울진군", "940": "울릉군" } }, "48": { name: "경남", sigungu: { "121": "창원시 의창구", "123": "창원시 성산구", "125": "창원시 마산합포구", "127": "창원시 마산회원구", "129": "창원시 진해구", "170": "진주시", "220": "통영시", "240": "사천시", "250": "김해시", "270": "밀양시", "310": "거제시", "330": "양산시", "720": "의령군", "730": "함안군", "740": "창녕군", "820": "고성군", "840": "남해군", "850": "하동군", "860": "산청군", "870": "함양군", "880": "거창군", "890": "합천군" } }, "50": { name: "제주특별자치도", sigungu: { "110": "제주시", "130": "서귀포시" } } };
const lhPhoneNumbers = { "서울": "02-3416-3600", "경기남부": "031-250-8380", "부산울산": "051-460-5401", "대구경북": "053-603-2640", "광주전남": "062-360-3114", "대전충남": "042-470-0117", "강원": "033-258-4400", "경남": "055-210-8680", "전북": "063-230-6100", "제주": "064-720-1000", "충북": "1600-1004 (통합 콜센터)" };

// closeModal 함수 정의
function closeModal() {
    modalWrapper.style.display = 'none';
}

window.onload = function() {
    // 변수 선언을 window.onload 내로 이동
    const closeModalBtn = document.getElementById('close-modal-btn');  // 여기로 이동
    setupRegionSearch();
    initializeMapAndLocation();

    closeModalBtn.addEventListener('click', closeModal);  // 이제 사용 가능
    modalWrapper.addEventListener('click', (e) => {
        if (e.target === modalWrapper) {
            closeModal();
        }
    });
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
    document.getElementById('info-lat').innerText = lat.toFixed(6);
    document.getElementById('info-lng').innerText = lng.toFixed(6);
    document.getElementById('info-addr').innerText = "확인 중...";
    document.getElementById('info-code').innerText = "확인 중...";
    geocoder.coord2RegionCode(lng, lat, (result, status) => {
        if (status === kakao.maps.services.Status.OK) {
            const bjdInfo = result.find(r => r.region_type === 'B');
            if (bjdInfo) {
                document.getElementById('info-addr').innerText = bjdInfo.address_name;
                const sidoCode = bjdInfo.code.substring(0, 2);
                const sigunguCode = bjdInfo.code.substring(2, 5);
                sidoSelect.value = sidoCode;
                updateSigunguSelect(sidoCode);
                sigunguSelect.value = sigunguCode;
                updateBjdCode();
            } else {
                document.getElementById('info-addr').innerText = "법정동 정보 없음";
                document.getElementById('info-code').innerText = "확인 불가";
            }
        } else {
            document.getElementById('info-addr').innerText = "위치 정보 확인 실패";
            document.getElementById('info-code').innerText = "확인 불가";
        }
    });
}

function updateBjdCode() {
    const sidoCode = sidoSelect.value;
    const sigunguCode = sigunguSelect.value;
    document.getElementById('info-code').innerText = sidoCode && sigunguCode ? `${sidoCode}${sigunguCode}` : "코드 없음";
}

function setupRegionSearch() {
    const searchBtn = document.getElementById('search-btn');
    const toggleAdvancedBtn = document.getElementById('toggle-advanced-search-btn');
    const advancedFilterPanel = document.getElementById('advanced-filter-panel');
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
    sidoSelect.addEventListener('change', () => { updateSigunguSelect(sidoSelect.value); updateBjdCode(); });
    sigunguSelect.addEventListener('change', updateBjdCode);

    toggleAdvancedBtn.addEventListener('click', () => {
        const isVisible = advancedFilterPanel.style.display === 'flex';
        advancedFilterPanel.style.display = isVisible ? 'none' : 'flex';
    });

    searchBtn.addEventListener('click', () => {
        updateBjdCode();
        loadingAnimation.style.display = 'block';
        listContent.style.display = 'none';
        fetch(`/api/housing?brtcCode=${sidoSelect.value}&signguCode=${sigunguSelect.value}&numOfRows=50`)
            .then(response => response.json())
            .then(data => {
                currentHousingList = data.hsmpList || [];
                clearMarkers();
                displayHousingOnMap(currentHousingList);
            })
            .catch(error => console.error('API 호출 중 오류:', error))
            .finally(() => {
                loadingAnimation.style.display = 'none';
                listContent.style.display = 'block';
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
// 기존의 displayHousingOnMap 함수 내부 중 찜하기 버튼 관련 부분 (수정 포함)
async function displayHousingOnMap(housingList) {
    const propertyList = document.getElementById('property-list');
    propertyList.innerHTML = ''; // 기존 목록 초기화

    if (!housingList || housingList.length === 0) {
        propertyList.innerHTML = '<li>검색 결과가 없습니다.</li>';
        return;
    }

    for (const item of housingList) {
        const geocodeResult = await new Promise((resolve) => {
            geocoder.addressSearch(item.rnAdres, (result, status) => {
                if (status === kakao.maps.services.Status.OK) {
                    resolve({
                        ...item,
                        coords: new kakao.maps.LatLng(result[0].y, result[0].x)
                    });
                } else {
                    resolve(null);
                }
            });
        });

        if (geocodeResult) {
            const { coords, ...itemData } = geocodeResult;

            const marker = new kakao.maps.Marker({
                map: map,
                position: coords,
                title: itemData.hsmpNm
            });

            const iwContent = `<div style="padding:5px;width:260px;"><strong>${itemData.hsmpNm}</strong><br><small>${itemData.rnAdres}</small></div>`;
            const infowindow = new kakao.maps.InfoWindow({
                content: iwContent,
                removable: true
            });

            markers.push(marker);

            const listItem = document.createElement('li');
            listItem.className = 'property-item';
            listItem.id = `property-${itemData.hsmpSn}`;
            listItem.dataset.pnu = itemData.hsmpSn;

            // 데이터 속성 추가
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
            listItem.dataset.msg = itemData.msg;

            const formattedDeposit = Number(itemData.bassRentGtn).toLocaleString('ko-KR');
            const formattedRent = Number(itemData.bassMtRntchrg).toLocaleString('ko-KR');

            // innerHTML로 정보 영역만 구성
            listItem.innerHTML = `
        <div class="property-info">
          <strong>${itemData.hsmpNm}</strong>
          <p>단지식별자: ${itemData.hsmpSn}</p>
          <p>주소: ${itemData.rnAdres}</p>
          <p>유형: ${itemData.suplyTyNm || '정보없음'} / ${typeof itemData.houseTyNm === 'object' ? '정보없음' : itemData.houseTyNm}</p>
          <p>전용면적: ${itemData.suplyPrvuseAr} ㎡</p>
          <p class="price">보증금: ${formattedDeposit}원 / 월세: ${formattedRent}원</p>
          <p>기관: <span class="instt-button">${itemData.insttNm}</span></p>
        </div>
      `;

            // 찜 버튼 직접 생성 (이벤트 전파 방지 위해)
            const button = document.createElement('button');
            button.id = `favorite-btn-${itemData.hsmpSn}-${itemData.suplyPrvuseAr}`;
            button.dataset.hsmpSn = itemData.hsmpSn;
            button.dataset.area = itemData.suplyPrvuseAr;
            button.textContent = '찜하기';

            button.addEventListener('click', (e) => {
                e.stopPropagation(); // 상위 클릭 이벤트 방지
                addToFavorites(itemData.hsmpSn, itemData.suplyPrvuseAr, e);
            });

            listItem.appendChild(button);
            propertyList.appendChild(listItem);

            // 마커 + 리스트 아이템 클릭 시 모달/포커싱
            const openInfoWindow = () => {
                if (activeInfoWindow) activeInfoWindow.close();
                infowindow.open(map, marker);
                activeInfoWindow = infowindow;
            };

            listItem.addEventListener('click', () => {
                openInfoWindow();
                map.panTo(coords);
                document.querySelectorAll('.property-item').forEach(li => li.classList.remove('active-item'));
                listItem.classList.add('active-item');
                populateAndShowModal(itemData.pnu);
            });

            kakao.maps.event.addListener(marker, 'click', () => {
                openInfoWindow();
                listItem.scrollIntoView({ behavior: 'smooth', block: 'start' });
                document.querySelectorAll('.property-item').forEach(li => li.classList.remove('active-item'));
                listItem.classList.add('active-item');
                populateAndShowModal(itemData.pnu);
            });
        }

        // 너무 빠르게 렌더링되지 않도록 약간 지연
        await new Promise(resolve => setTimeout(resolve, 50));
    }

    console.log("매물 정보 표시 완료.");

    // 모든 항목 렌더링 후 찜 상태 체크
    checkLoginStatus();
}





closeModalBtn.addEventListener('click', () => {
    modalWrapper.style.display = 'none';
});
modalWrapper.addEventListener('click', (e) => {
    if (e.target === modalWrapper) {
        modalWrapper.style.display = 'none';
    }
});

function populateAndShowModal(pnu) {
    const data = currentHousingList.find(item => item.pnu === pnu);
    data.hsmpNm = undefined;
    if (!data) {
        alert("상세 정보를 찾는 데 실패했습니다.");
        return;
    }

    const checkValue = (value) => {
        if (!value || typeof value === 'object') return '정보없음';
        return value;
    };
    const formatNumber = (value) => {
        const num = Number(value);
        if (!num) return '정보없음';
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
                    <tr><th>단지식별자</th><td>${data.hsmpSn}</td></tr>
                    <tr><th>단지명</th><td>${data.hsmpNm}</td></tr>
                    <tr><th>주소</th><td>${data.rnAdres}</td></tr>
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