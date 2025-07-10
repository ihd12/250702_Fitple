// 기본 경도와 위도, 서울시 종로구(위치 정보 없을 시)
let latitude = 37.566826,
    longitude = 126.9786567,
    geocoder, map, markers = [],
    activeInfoWindow = null,
    currentHousingList = [],
    selectedHouseType = "",
    // 기본 덧글 페이지 1페이지부터 시작
    currentReplyPage = 1,
    // 페이지당 최대 덧글 기본 5
    repliesPerPage = 5,
    allReplies = [];
// 테스트용 유저 아이디, 향후에 제거 ★
const currentUserId = "testUser123";
const sidoSelect = document.getElementById('sido-select'),
    sigunguSelect = document.getElementById('sigungu-select'),
    modalWrapper = document.getElementById('modal-wrapper'),
    modalBody = document.getElementById('modal-body'),
    closeModalBtn = document.getElementById('close-modal-btn');
// 법정동 코드. 추후 변경에 따라 수정.
const regionData = {
    "11": {
        name: "서울특별시",
        sigungu: {
            "110": "종로구",
            "140": "중구",
            "170": "용산구",
            "200": "성동구",
            "215": "광진구",
            "230": "동대문구",
            "260": "중랑구",
            "290": "성북구",
            "305": "강북구",
            "320": "도봉구",
            "350": "노원구",
            "380": "은평구",
            "410": "서대문구",
            "440": "마포구",
            "470": "양천구",
            "500": "강서구",
            "530": "구로구",
            "545": "금천구",
            "560": "영등포구",
            "590": "동작구",
            "620": "관악구",
            "650": "서초구",
            "680": "강남구",
            "710": "송파구",
            "740": "강동구"
        }
    },
    "26": {
        name: "부산광역시",
        sigungu: {
            "110": "중구",
            "140": "서구",
            "170": "동구",
            "200": "영도구",
            "230": "부산진구",
            "260": "동래구",
            "290": "남구",
            "320": "북구",
            "350": "해운대구",
            "380": "사하구",
            "410": "금정구",
            "440": "강서구",
            "470": "연제구",
            "500": "수영구",
            "530": "사상구",
            "710": "기장군"
        }
    },
    "27": {
        name: "대구광역시",
        sigungu: {
            "110": "중구",
            "140": "동구",
            "170": "서구",
            "200": "남구",
            "230": "북구",
            "260": "수성구",
            "290": "달서구",
            "710": "달성군"
        }
    },
    "28": {
        name: "인천광역시",
        sigungu: {
            "110": "중구",
            "140": "동구",
            "177": "미추홀구",
            "185": "연수구",
            "200": "남동구",
            "237": "부평구",
            "245": "계양구",
            "260": "서구",
            "710": "강화군",
            "720": "옹진군"
        }
    },
    "29": {
        name: "광주광역시",
        sigungu: {
            "110": "동구",
            "140": "서구",
            "155": "남구",
            "170": "북구",
            "200": "광산구"
        }
    },
    "30": {
        name: "대전광역시",
        sigungu: {
            "110": "동구",
            "140": "중구",
            "170": "서구",
            "200": "유성구",
            "230": "대덕구"
        }
    },
    "31": {
        name: "울산광역시",
        sigungu: {
            "110": "중구",
            "140": "남구",
            "170": "동구",
            "200": "북구",
            "710": "울주군"
        }
    },
    "36": {
        name: "세종특별자치시",
        sigungu: {
            "110": "세종특별자치시"
        }
    },
    "41": {
        name: "경기",
        sigungu: {
            "111": "수원시 장안구",
            "113": "수원시 권선구",
            "115": "수원시 팔달구",
            "117": "수원시 영통구",
            "131": "성남시 수정구",
            "133": "성남시 중원구",
            "135": "성남시 분당구",
            "150": "의정부시",
            "171": "안양시 만안구",
            "173": "안양시 동안구",
            "190": "부천시",
            "210": "광명시",
            "220": "평택시",
            "250": "동두천시",
            "271": "안산시 상록구",
            "273": "안산시 단원구",
            "281": "고양시 덕양구",
            "285": "고양시 일산동구",
            "287": "고양시 일산서구",
            "290": "과천시",
            "310": "구리시",
            "360": "남양주시",
            "370": "오산시",
            "390": "시흥시",
            "410": "군포시",
            "430": "의왕시",
            "450": "하남시",
            "461": "용인시 처인구",
            "463": "용인시 기흥구",
            "465": "용인시 수지구",
            "480": "파주시",
            "500": "이천시",
            "550": "안성시",
            "570": "김포시",
            "590": "화성시",
            "610": "광주시",
            "630": "양주시",
            "650": "포천시",
            "670": "여주시",
            "800": "연천군",
            "820": "가평군",
            "830": "양평군"
        }
    },
    "51": {
        name: "강원특별자치도",
        sigungu: {
            "110": "춘천시",
            "130": "원주시",
            "150": "강릉시",
            "170": "동해시",
            "190": "태백시",
            "210": "속초시",
            "230": "삼척시",
            "720": "홍천군",
            "730": "횡성군",
            "750": "영월군",
            "760": "평창군",
            "770": "정선군",
            "780": "철원군",
            "790": "화천군",
            "800": "양구군",
            "810": "인제군",
            "820": "고성군",
            "830": "양양군"
        }
    },
    "43": {
        name: "충북",
        sigungu: {
            "111": "청주시 상당구",
            "112": "청주시 서원구",
            "113": "청주시 흥덕구",
            "114": "청주시 청원구",
            "130": "충주시",
            "150": "제천시",
            "720": "보은군",
            "730": "옥천군",
            "740": "영동군",
            "745": "증평군",
            "750": "진천군",
            "760": "괴산군",
            "770": "음성군",
            "800": "단양군"
        }
    },
    "44": {
        name: "충남",
        sigungu: {
            "131": "천안시 동남구",
            "133": "천안시 서북구",
            "150": "공주시",
            "180": "보령시",
            "200": "아산시",
            "210": "서산시",
            "230": "논산시",
            "250": "계룡시",
            "270": "당진시",
            "710": "금산군",
            "760": "부여군",
            "770": "서천군",
            "790": "청양군",
            "800": "홍성군",
            "810": "예산군",
            "825": "태안군"
        }
    },
    "52": {
        name: "전북특별자치도",
        sigungu: {
            "111": "전주시 완산구",
            "113": "전주시 덕진구",
            "130": "군산시",
            "140": "익산시",
            "180": "정읍시",
            "190": "남원시",
            "210": "김제시",
            "710": "완주군",
            "720": "진안군",
            "730": "무주군",
            "740": "장수군",
            "750": "임실군",
            "770": "순창군",
            "790": "고창군",
            "800": "부안군"
        }
    },
    "46": {
        name: "전남",
        sigungu: {
            "110": "목포시",
            "130": "여수시",
            "150": "순천시",
            "170": "나주시",
            "230": "광양시",
            "710": "담양군",
            "720": "곡성군",
            "730": "구례군",
            "770": "고흥군",
            "780": "보성군",
            "800": "화순군",
            "810": "장흥군",
            "820": "강진군",
            "830": "해남군",
            "840": "영암군",
            "860": "무안군",
            "870": "함평군",
            "880": "영광군",
            "890": "장성군",
            "900": "완도군",
            "910": "진도군"
        }
    },
    "47": {
        name: "경북",
        sigungu: {
            "111": "포항시 남구",
            "113": "포항시 북구",
            "130": "경주시",
            "150": "김천시",
            "170": "안동시",
            "190": "구미시",
            "210": "영주시",
            "230": "영천시",
            "250": "상주시",
            "280": "문경시",
            "290": "경산시",
            "720": "군위군",
            "730": "의성군",
            "750": "청송군",
            "760": "영양군",
            "770": "영덕군",
            "820": "청도군",
            "830": "고령군",
            "840": "성주군",
            "850": "칠곡군",
            "900": "예천군",
            "920": "봉화군",
            "930": "울진군",
            "940": "울릉군"
        }
    },
    "48": {
        name: "경남",
        sigungu: {
            "121": "창원시 의창구",
            "123": "창원시 성산구",
            "125": "창원시 마산합포구",
            "127": "창원시 마산회원구",
            "129": "창원시 진해구",
            "170": "진주시",
            "220": "통영시",
            "240": "사천시",
            "250": "김해시",
            "270": "밀양시",
            "310": "거제시",
            "330": "양산시",
            "720": "의령군",
            "730": "함안군",
            "740": "창녕군",
            "820": "고성군",
            "840": "남해군",
            "850": "하동군",
            "860": "산청군",
            "870": "함양군",
            "880": "거창군",
            "890": "합천군"
        }
    },
    "50": {
        name: "제주특별자치도",
        sigungu: {
            "110": "제주시",
            "130": "서귀포시"
        }
    }
};
// 공인중개사를 대체하기 위해 각 지역 LH에 맞는 전화번호
const lhPhoneNumbers = {
    "서울": "02-3416-3600",
    "경기남부": "031-250-8380",
    "부산울산": "051-460-5401",
    "대구경북": "053-603-2640",
    "광주전남": "062-360-3114",
    "대전충남": "042-470-0117",
    "강원": "033-258-4400",
    "경남": "055-210-8680",
    "전북": "063-230-6100",
    "제주": "064-720-1000",
    "충북": "1600-1004 (통합 콜센터)"
};

// ▼▼▼ kakao.maps.load를 사용하여 SDK 로드 후 앱 시작 ▼▼▼
kakao.maps.load(() => {
    geocoder = new kakao.maps.services.Geocoder();
    setupRegionSearch();
    initializeMapAndLocation();
    setupReplySubmitListener();
});

// 초기 위치 정보 확인 및 수집. 치 정보 삭제까지 고정 혹은 거부 혹은 미확인시  기본 위치(서울시 종로구).
function initializeMapAndLocation() {
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(pos => {
            latitude = pos.coords.latitude;
            longitude = pos.coords.longitude;
            drawMap();
        }, error => {
            console.error("Geolocation 에러:", error.message);
            alert("현재 위치를 가져올 수 없습니다. 기본 위치로 지도를 표시합니다.");
            drawMap();
        });
    } else {
        alert("이 브라우저에서는 위치 정보 기능을 지원하지 않습니다.");
        drawMap();
    }
}

// 위도와 경도를 확인해서 지도의 중심 위치로 처리
function drawMap() {
    // mapContainer: 지도가 표시될 HTML 요소
    // mapOption: 지도의 초기 설정(예: 중심 좌표, 확대 레벨 등)을 담고 있는 객체
    const mapContainer = document.getElementById('map');
    const mapOption = {
        // 지도의 중심을 현 위도와 경도
        center: new kakao.maps.LatLng(latitude, longitude),
        // 기본 고도 레벨 : 8
        level: 8
    };
    map = new kakao.maps.Map(mapContainer, mapOption);
    kakao.maps.event.addListener(map, 'tilesloaded', function () {
        displayLocationInfo(latitude, longitude);
    });
    // 저장된 위치 정보에 따라서 그 위치로 지도의 화면을 옮겨주는 요소.
    kakao.maps.event.addListener(map, 'idle', function () {
        const newCenter = map.getCenter();
        if (newCenter.getLat() !== latitude || newCenter.getLng() !== longitude) {
            latitude = newCenter.getLat();
            longitude = newCenter.getLng();
            displayLocationInfo(latitude, longitude);
        }
    });
}

// 메뉴에 카카오 지도 Geocoder를 이용해 위도와 경도를 바탕으로 법정동 코드 및 행정주소 조회
function displayLocationInfo(lat, lng) {
    // 위도lat와 경도lng 값을 소수점 6자리까지 포매팅하고, info-lat에 해당하는 내용에 업데이트
    document.getElementById('info-lat').innerText = lat.toFixed(6);
    document.getElementById('info-lng').innerText = lng.toFixed(6);
    // 지연, 로딩 등으로 데이터를 바로 가져오지 못할 경우에 메세지
    document.getElementById('info-addr').innerText = "확인 중...";
    document.getElementById('info-code').innerText = "확인 중...";
    // 카카오톡의 지오코딩 과정을 통해서 법정동 코드와 행정 주소를 가져오는 중요한 부분 ★★★★★
    geocoder.coord2RegionCode(lng, lat, (result, status) => {
        if (status === kakao.maps.services.Status.OK) {
            const bjdInfo = result.find(r => r.region_type === 'B');
            if (bjdInfo) {
                document.getElementById('info-addr').innerText = bjdInfo.address_name;
                // 받아온 bjd 코드를 시도 코드와 시군구 코드로 분류함.
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

// 광역시도의 2자리 숫자와 산하 구군 단위의 3자리 숫자를 결합해서 업데이트하는 코드.
// 정부 API에서 법정동 코드를 기반으로 검색하므로 매우 중요함.
function updateBjdCode() {
    const sidoCode = sidoSelect.value;
    const sigunguCode = sigunguSelect.value;
    document.getElementById('info-code').innerText = sidoCode && sigunguCode ? `${sidoCode}${sigunguCode}` : "코드 없음";
}

// 1. 검색 기능.
function setupRegionSearch() {
    // 기본 검색
    const searchBtn = document.getElementById('search-btn');
    // 세부 검색 토글창 및 세부 검색 관련 내용(세부 검색 적용, 초기화)
    const toggleAdvancedBtn = document.getElementById('toggle-advanced-search-btn');
    const advancedFilterPanel = document.getElementById('advanced-filter-panel');
    const applyFilterBtn = document.getElementById('apply-filter-btn');
    const resetFilterBtn = document.getElementById('reset-filter-btn');
    // 검색 중 로그인 표시
    const loadingAnimation = document.getElementById('loading-animation');
    // 검색 완료 매물를 출력하는 콘테이너
    const listContent = document.getElementById('list-content');
    // 세부 검색 내용. 면적 주택 유형 등등
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
    // 시도구군이 적힌 드롭다운에 따른 내용 변경
    sidoSelect.addEventListener('change', () => {
        updateSigunguSelect(sidoSelect.value);
        updateBjdCode();
    });
    // 세부 검색 관련
    sigunguSelect.addEventListener('change', updateBjdCode);
    toggleAdvancedBtn.addEventListener('click', () => {
        const isVisible = advancedFilterPanel.style.display === 'flex';
        advancedFilterPanel.style.display = isVisible ? 'none' : 'flex';
    });
    // 검샏 버튼 클릭 이벤트
    searchBtn.addEventListener('click', () => {
        updateBjdCode();
        // 검색 하는 동안 로딩 애니메이션
        loadingAnimation.style.display = 'block';
        // 검색 완료시 화면에 출력
        listContent.style.display = 'none';

        // 중요. 평균 별점 등을 계산하기 위해서 데이터에 검색 내용을 받아와서 propertyId로 조회 + 너무 많은 호출 염려해서 최대 호출수 제한을 위함
        const numOfRows = document.getElementById('num-of-rows-select').value;
        const sidoCode = sidoSelect.value;
        const sigunguCode = sigunguSelect.value;

        // 임대주택 API 법정정코드(시도2자리,구군3자리)와 불러올 매물의 최대 숫자를 요청
        fetch(`/api/housing?brtcCode=${sidoCode}&signguCode=${sigunguCode}&numOfRows=${numOfRows}`).then(response => response.json()).then(data => {
            currentHousingList = data.hsmpList || []; // 현재 검색 결과 저장
            clearMarkers(); // 검색 결과 외의 이전 마커들 제거
            displayHousingOnMap(currentHousingList); // 지도에 리스트 정보 표시
        }).catch(error => console.error('API 호출 중 오류:', error)).finally(() => {
            loadingAnimation.style.display = 'none';
            listContent.style.display = 'block';
        });
    });
    // 기본 면적 제시 버튼
    areaPresetButtons.forEach(button => {
        button.addEventListener('click', function () {
            minAreaInput.value = this.dataset.min; // 클릭된 버튼의 data-min 값을 최소 면적 입력 필드에 설정
            maxAreaInput.value = this.dataset.max; // 클릭된 버튼의 data-max 값을 최대 면적 입력 필드에 설정
            applyFilterBtn.click();
        });
    });
    // 아파트, 다세대 등에 따른 버튼
    typeFilterButtons.forEach(button => {
        button.addEventListener('click', function () {
            typeFilterButtons.forEach(btn => btn.classList.remove('active')); // 모든 유형 버튼에서 'active' 클래스 제거
            this.classList.add('active'); // 클릭된 현재 버튼에 'active' 클래스 추가
            selectedHouseType = this.dataset.type; // 클릭된 버튼의 data-type 값을 selectedHouseType 변수에 저장
            applyFilterBtn.click(); // '필터 적용' 버튼을 프로그래밍 방식으로 클릭하여 필터링 시작
        });
    });
    // 세부 검색 관련 내용 ★★★★★★★★★★
    applyFilterBtn.addEventListener('click', () => {
        // 보증금(최소, 최대)
        const minDeposit = document.getElementById('min-deposit').value,
            maxDeposit = document.getElementById('max-deposit').value,
            // 월세(최소, 최대)
            minRent = document.getElementById('min-rent').value,
            maxRent = document.getElementById('max-rent').value,
            // 면적(최소, 최대)
            minArea = document.getElementById('min-area').value,
            maxArea = document.getElementById('max-area').value;
        // 기본적으로 1만원 단위에 해당하므로 각 값들을 1만워 단위로 조정해줌.
        const minDepositValue = minDeposit ? parseInt(minDeposit) * 10000 : 0,
            maxDepositValue = maxDeposit ? parseInt(maxDeposit) * 10000 : Infinity,
            minRentValue = minRent ? parseInt(minRent) * 10000 : 0,
            maxRentValue = maxRent ? parseInt(maxRent) * 10000 : Infinity,
            minAreaValue = minArea ? parseFloat(minArea) : 0,
            maxAreaValue = maxArea ? parseFloat(maxArea) : Infinity;
        // 매물의 데이터와 세부 검색 값들을 대조함
        const filteredList = currentHousingList.filter(item => {
            const deposit = Number(item.bassRentGtn),
                rent = Number(item.bassMtRntchrg),
                area = parseFloat(item.suplyPrvuseAr),
                houseType = typeof item.houseTyNm === 'string' ? item.houseTyNm : '';
            const isDepositInRange = deposit >= minDepositValue && deposit <= maxDepositValue,
                isRentInRange = rent >= minRentValue && rent <= maxRentValue,
                isAreaInRange = area >= minAreaValue && area <= maxAreaValue;
            // 주택 유형에 따른 분류. 주택 유형이 없을 경우에는 etc로 취급
            let isTypeMatch = true;
            if (selectedHouseType) {
                const mainTypes = ['다가구주택', '다세대주택', '아파트', '오피스텔'];
                if (selectedHouseType === 'etc') {
                    isTypeMatch = houseType && !mainTypes.includes(houseType);
                } else {
                    isTypeMatch = houseType === selectedHouseType;
                }
            }
            // 각 세부 검색 분류에 따른 최종 결과를 반환
            return isDepositInRange && isRentInRange && isAreaInRange && isTypeMatch;
        });
        clearMarkers(); // 그에 따라서 최종 결과에 해당하는 매물 외의 지도의 마커를 제거
        displayHousingOnMap(filteredList); // 동시에 최종 결과에 해당하는 매물만을 지도에 표현
    });
    // 상세 검색 초기화 - 버튼 클릭할 시 기존의 값들을 ''으로 바꾸고, 활성화 된 버튼을 비활성화함.
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
        clearMarkers(); // 검색에 따라서 분류된 내용들의 마커를 제거하고,
        displayHousingOnMap(currentHousingList); // 다시 검색 이전의 분류 이전 리스트 기반으로 출력함.
    });
    updateSigunguSelect(sidoSelect.value);
}

function updateSigunguSelect(sidoCode) {
    sigunguSelect.innerHTML = ''; // 1. 기존 시/군/구 옵션 모두 제거

    const sigunguList = regionData[sidoCode]?.sigungu || {};

    // 2. 시/군/구 목록을 기반으로 새로운 옵션 추가
    for (const code in sigunguList) {
        const option = document.createElement('option');
        option.value = code;
        option.innerText = sigunguList[code];
        sigunguSelect.appendChild(option);
    }
}

function clearMarkers() {
    // 1. 지도에 표시된 모든 마커 제거
    // 'markers' 배열에 저장된 각 마커 객체를 순회
    for (var i = 0; i < markers.length; i++) {
        markers[i].setMap(null); // 각 마커를 지도에서 제거 (null을 인자로 전달)
    }

    // 2. 마커 배열 초기화
    markers = []; // 'markers' 배열을 빈 배열로 초기화하여 이전 마커 참조를 제거

    // 3. 활성화된 인포윈도우 닫기 및 초기화
    if (activeInfoWindow) { // 'activeInfoWindow' 변수에 인포윈도우 객체가 저장되어 있다면
        activeInfoWindow.close(); // 해당 인포윈도우 닫기
        activeInfoWindow = null; // 'activeInfoWindow' 변수를 null로 초기화
    }
}

async function displayHousingOnMap(housingList) {
    const propertyList = document.getElementById('property-list');
    propertyList.innerHTML = ''; // 1. 기존 주택 목록 초기화

    // 2. 검색 결과가 없는 경우 처리
    if (!housingList || housingList.length === 0) {
        propertyList.innerHTML = '<li>검색 결과가 없습니다.</li>'; // 사용자에게 메시지 표시
        return; // 함수 실행 종료
    }

    // 3. 각 주택 항목에 대해 반복 처리
    for (const item of housingList) {
        // 4. 주소(rnAdres)를 좌표로 변환 (지오코딩)
        const geocodeResult = await new Promise(resolve => {
            geocoder.addressSearch(item.rnAdres, (result, status) => {
                if (status === kakao.maps.services.Status.OK) {
                    // 성공 시, 원본 아이템 데이터에 좌표(coords)를 추가하여 반환
                    resolve({
                        ...item, // 기존 item의 모든 속성을 복사
                        coords: new kakao.maps.LatLng(result[0].y, result[0].x) // 변환된 좌표 추가
                    });
                } else {
                    // 실패 시 null 반환
                    console.warn(`주소 지오코딩 실패: ${item.rnAdres} (${status})`);
                    resolve(null);
                }
            });
        });

        // 5. 지오코딩 결과가 유효하면 마커 생성 및 지도에 표시
        if (geocodeResult) {
            const {
                coords, // 지오코딩된 좌표
                ...itemData // 원본 주택 정보 (coords 제외)
            } = geocodeResult; // 구조 분해 할당을 통해 coords와 나머지 데이터를 분리

            const marker = new kakao.maps.Marker({
                map: map,          // 마커를 표시할 지도 객체 (전역 변수 'map')
                position: coords,  // 마커의 위치 (지오코딩된 좌표)
                title: itemData.hsmpNm // 마커에 마우스를 올렸을 때 나타날 이름 (주택 이름)
            });

            markers.push(marker); // 1. 마커 배열에 추가
            const listItem = document.createElement('li'); // 목록아이템 li 생성
            listItem.className = 'property-item'; // CSS 클래스 추가
            // 3.  고유한 propertyId 추가(매물 식별)
            listItem.dataset.propertyId = `${itemData.hsmpSn}-${itemData.styleNm}-${itemData.suplyCmnuseAr}`;
            // 4. 보증금Deposit와 월세Rent를 한국식으로 포매팅
            const formattedDeposit = Number(itemData.bassRentGtn).toLocaleString('ko-KR'),
                formattedRent = Number(itemData.bassMtRntchrg).toLocaleString('ko-KR');
            // 5. 평점 정보 html 생성
            let ratingHtml = `<p class="rating-display">평점 없음</p>`; // 기본값
            if (itemData.ratingCount > 0) { // 만약 평점이 0보다 크다면(평점은 최소 1점 이상)
                const averageRating = itemData.averageRating || 0; // 평균 평점(averageRatin)을 가져옴
                ratingHtml = `<p class="rating-display" title="평균 ${averageRating.toFixed(1)}점">
                                        <span class="star-filled">${'★'.repeat(Math.round(averageRating))}</span><span class="star-empty">${'☆'.repeat(5 - Math.round(averageRating))}</span>
                                        <strong>${averageRating.toFixed(1)}</strong> (${itemData.ratingCount}명)
                                     </p>` // 별, 평균, 참여자수를 표시
            }
            // 6. 매물 정보listItem의 구축
            // 값이 없다면 정보 없음{}으로 표시하도록 함.
            listItem.innerHTML = `<strong>${itemData.hsmpNm}</strong>${ratingHtml}
            <p>주소: ${itemData.rnAdres}</p>
            <p>유형: ${itemData.suplyTyNm || "정보없음"} / ${typeof itemData.houseTyNm === 'object' ? "정보없음" : itemData.houseTyNm}</p>
            <p>전용면적: ${itemData.suplyPrvuseAr} ㎡</p>
            <p class="price">보증금: ${formattedDeposit}원 / 월세: ${formattedRent}원</p>
            <p>기관: <span class="instt-button">${itemData.insttNm}</span></p>`;
            propertyList.appendChild(listItem); // 구성된 listItem을 목록에 추가
            const openInfoWindow = () => {
                // 1. 기존의 인포 윈도우가 열려있다면 닫음.
                // 인포 윈도우 = 지도에 표시 되는 마커의 상세내용
                if (activeInfoWindow) {
                    activeInfoWindow.close()
                }
                // 2. 매물의 이름과 주소를 포함하는 html 문자열 생성
                // 지도에 표시될 인포 윈도우에 나타남.
                const iwContent = `<div style="padding:5px;width:260px;"><strong>${itemData.hsmpNm}</strong><br><small>${itemData.rnAdres}</small></div>`,
                    // 위에서 정의한 인포 윈도우의 객체 생성
                    infowindow = new kakao.maps.InfoWindow({
                        content: iwContent, // 위에서 정의한 내용 표시
                        removable: !0 // 사용자가 닫을 수 있도록 직접 제어 가능한 X자 추가
                    });
                // 지도와 마커를 연결해서 infowindow를 엶
                infowindow.open(map, marker);
                // 현재 열린 infowindow를 저장해서 향후에 새로운 것을 열 때 이전 것이 닫히게 함.
                activeInfoWindow = infowindow
            };
            // 매물 리스트의 매물을 누를 시 이벤트
            listItem.addEventListener('click', () => {
                // 매물에 해당하는 인포윈도우
                openInfoWindow();
                // 매물의 좌표(coords)를 중앙으로 해서 지도 이동
                map.panTo(coords);
                // 목록의 매물 아이템 활성화하고자, 이전에 활성화 된 아이템의 활성화를 해제
                document.querySelectorAll('.property-item').forEach(li => li.classList.remove('active-item'));
                // 현재 클릭된 아이템을 활성화
                listItem.classList.add('active-item');
                // 매물의 상세 정보를 모달(팝업)을 열면서 내용을 채운다.
                populateAndShowModal(itemData)
            });
            kakao.maps.event.addListener(marker, 'click', () => {
                openInfoWindow();
                //  scrollIntoView() 메소드는 scrollIntoView()가 호출 된 요소가 사용자에게 표시되도록 요소의 상위 컨테이너를 스크롤
                listItem.scrollIntoView({
                    // 스크롤 : smooth / insert 즉시, auto 등 있음
                    behavior: 'smooth',
                    // 수직 정렬을 정의합니다. start, center, end, 또는 nearest 중 하나입니다. 기본 값은 start입니다.
                    block: 'start'
                });
                document.querySelectorAll('.property-item').forEach(li => li.classList.remove('active-item'));
                listItem.classList.add('active-item');
                populateAndShowModal(itemData)
            })
        }
        // 각 매물 처리가 끝날 때마다 50밀리초(0.05초) 동안 실행을 일시 중지해 부드러움 체감 ▲
        await new Promise(resolve => setTimeout(resolve, 50))
    }
}

// 닫기 누를 시 모달창이 닫힘 / 닫는 방법 1
closeModalBtn.addEventListener('click', () => {
    modalWrapper.style.display = 'none'
});

// 모달창 밖의 화면을 누를시 모달창이 닫힘 / 닫는 방법 2 / 제거할 X로만 닫을 수 있음.
modalWrapper.addEventListener('click', e => {
    if (e.target === modalWrapper) {
        modalWrapper.style.display = 'none'
    }
});

// 모달창에 상세 정보
async function populateAndShowModal(itemData) {
    // value의 유효성 검사
    const checkValue = value => !value || typeof value === 'object' ? '정보없음' : value,
        // 숫자 포매팅 헬퍼 함수(한국 통화 기준)
        formatNumber = value => isNaN(Number(value)) ? '정보없음' : Number(value).toLocaleString('ko-KR'),
        // 기관명에 따른 전화번호 찾기(LH 지역 공사 기준
        findPhoneNumber = insttNm => {
            if (!insttNm) return '1600-1004 (통합 콜센터)';
            for (const region in lhPhoneNumbers) {
                if (insttNm.includes(region)) return lhPhoneNumbers[region]
            }
            // 일치하지 않으면 통합 콜센터 기준으로 반환.
            return '1600-1004 (통합 콜센터)'
        },
        // 매물 중개 기관의 실제 전화번호 찾기
        entrpsTel = findPhoneNumber(itemData.insttNm),
        // 매물의 고유 번호PropertyId(별점, 댓글 기능 등에 참고)
        propertyId = `${itemData.hsmpSn}-${itemData.styleNm}-${itemData.suplyCmnuseAr}`;

    // 아래는 상세보기 모달의 div용.
    modalBody.innerHTML = `
            <table>
                <tr><th>단지명</th><td>${checkValue(itemData.hsmpNm)}</td></tr>
                <tr><th>주소</th><td>${checkValue(itemData.rnAdres)}</td></tr>
                <tr><th>준공일자</th><td>${checkValue(itemData.competDe)}</td></tr>
                <tr><th>세대수</th><td>${checkValue(itemData.hshldCo)}</td></tr>
                <tr><th>공급유형</th><td>${checkValue(itemData.suplyTyNm)}</td></tr>
                <tr><th>전용면적</th><td>${checkValue(itemData.suplyPrvuseAr)} ㎡</td></tr>
                <tr><th>공용면적</th><td>${checkValue(itemData.suplyCmnuseAr)} ㎡</td></tr>
                <tr><th>주택유형</th><td>${checkValue(itemData.houseTyNm)}</td></tr>
                <tr><th>보증금</th><td>${formatNumber(itemData.bassRentGtn)} 원</td></tr>
                <tr><th>월임대료</th><td>${formatNumber(itemData.bassMtRntchrg)} 원</td></tr>
                <tr><th>전환보증금</th><td>${formatNumber(itemData.bassCnvrsGtnLmt)} 원</td></tr>
                <tr><th>관리 기관</th><td>${checkValue(itemData.insttNm)}</td></tr>
                <tr><th>연락처</th><td>${entrpsTel}</td></tr>
            </table>
            <div class="star-rating-section">
                <h4>매물 별점 주기</h4>
                <div class="stars" data-rating="0" data-property-id="${propertyId}">
                    <span class="star" data-value="1">☆</span><span class="star" data-value="2">☆</span><span class="star" data-value="3">☆</span><span class="star" data-value="4">☆</span><span class="star" data-value="5">☆</span>
                </div>
                <div><p class="rating-feedback"></p></div>
            </div>
            <div class="image-gallery-container">
                <div class="gallery-header"><div class="header-left"><span class="image-title">매물 이미지</span></div></div>
                <div class="main-image-viewer"><button class="nav-arrow prev-arrow">‹</button><img id="current-image" src="" alt="Current Image"><button class="nav-arrow next-arrow">›</button><div class="image-counter"><span id="current-image-index"></span> / <span id="total-images"></span></div></div>
                <div class="thumbnail-gallery"><div class="thumbnail-scroll-wrapper"></div></div>
            </div>
            <div id="reply-section">
                <h4>댓글</h4><ul id="reply-list"></ul><div id="reply-pagination"></div>
                <form id="reply-form" onsubmit="return false;">
                    <textarea id="reply-textarea" placeholder="댓글을 입력하세요." rows="2"></textarea>
                    <button id="reply-submit-btn" type="submit">등록</button>
                </form>
            </div>
            `;
    initGallery();
    document.getElementById('reply-list').dataset.propertyId = propertyId;
    fetchReplies(propertyId);
    try {
        // currentUserId는 차후에 멤버 기능과 연동할 시 제거할 예정입니다.
        const response = await fetch(`/api/ratings?propertyId=${propertyId}&userId=${currentUserId}`);
        let userRating = 0;
        if (response.status === 200) {
            const data = await response.json();
            userRating = data.ratingScore
        }
        setupStarRating(propertyId, userRating)
    } catch (error) {
        console.error("사용자 평점 조회 실패:", error);
        setupStarRating(propertyId, 0)
    }
    modalWrapper.style.display = 'flex'
}

// 매물의 별점을 계산합니다.
function updateListItemRating(propertyId, ratingInfo) {
    // 매물 검색시에 데이타베이스에 등록된 매물들의 propertyId를 바탕으로 구분
    const listItem = document.querySelector(`.property-item[data-property-id="${propertyId}"]`);
    if (!listItem) return;
    const ratingDisplay = listItem.querySelector('.rating-display');
    // 기본적으로 평점 없음(0점)으로 취급
    let ratingHtml = `<p class="rating-display">평점 없음</p>`;
    // 평점이 0보다 높을 시에는 평점 계산을 통해서 매물에 즉시 나타납니다.
    if (ratingInfo && ratingInfo.ratingCount > 0) {
        const averageRating = ratingInfo.averageRating || 0;
        ratingHtml = `<p class="rating-display" title="평균 ${averageRating.toFixed(1)}점">
                        <span class="star-filled">${'★'.repeat(Math.round(averageRating))}</span><span class="star-empty">${'☆'.repeat(5 - Math.round(averageRating))}</span>
                        <strong>${averageRating.toFixed(1)}</strong> (${ratingInfo.ratingCount}명)
                     </p>` // 평균 별점, 평가한 숫자 등을 계산해서 구분합니디.
    }
    ratingDisplay.outerHTML = ratingHtml
}

function setupStarRating(propertyId, initialRating) {
    // 필요한 dom 요소
    const starContainer = document.querySelector('.stars'), // 별점 전체를 감싸는 콘테이너
        stars = starContainer.querySelectorAll('.star'), // 개별 별점
        feedback = document.querySelector('.rating-feedback'); // 혹시 모를 별점 피드백 기능(비활성하)
    starContainer.dataset.rating = initialRating; // starContainer에 초기 별점값 저장
    updateStarsUI(initialRating); // 초기 별점값에 따라서 초기 별점 ui를 변경함
    starContainer.addEventListener('mouseover', e => { // 매물 별점 주기의 별 위에 마우스가 올라갈 시 이를 인식함
        if (e.target.classList.contains('star')) {
            // 각 별에 할당횐 숫자(별 위치에 따른 점수 1~5)의 밸류 가져오기.
            const hoverValue = e.target.dataset.value;
            // 마우스가 올라간 별의 이전 별들도 하이라이트 될 수 있도록 해서 시각적 효과 부여.
            stars.forEach(s => s.classList.toggle('hover', s.dataset.value <= hoverValue))
        }
    });
    // 마우스가 별점 컨테이너를 벗어나면 hover 효과를 모든 별에서 제거하는 이벤트
    starContainer.addEventListener('mouseout', () => {
        stars.forEach(s => s.classList.remove('hover'))
    });
    // 별점 등록 이벤트(click)
    starContainer.addEventListener('click', e => {
        // 클릭된 요소가 별인지 확인하여,
        if (e.target.classList.contains('star')) {
            const newRating = parseInt(e.target.dataset.value); // 별의 값을 정수로 변환.
            feedback.textContent = '저장 중...';
            // 서버에 평점 정보 전송
            fetch('/api/ratings', {
                // http POST, json 형식
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ // json 문자열
                    userId: currentUserId, // 현재 사용자 아이디
                    propertyId: propertyId, // 현재 매물의 ID
                    ratingScore: newRating // 사용자가 부여한 새로운 평점
                })
            }).then(response => {
                // 서버 응답이 OK가 아니라면 오류 발생으로 처리함.
                if (!response.ok) throw new Error(`서버 응답 오류: ${response.status}`);
                return response.json() // 응답 반환은 json 형식으로 파싱
            }).then(newRatingInfo => { // 새 별점 평가 정보를 받았을 경우.
                console.log("서버로부터 새로운 평점 정보 수신:", newRatingInfo);
                starContainer.dataset.rating = newRating; // 별점 콘테이너 내 데이터 업데이트
                updateStarsUI(newRating); // UI(별의 갯수에 해당하는 디자인)를 변경
                feedback.textContent = `✓ ${newRating}점이 등록되었습니다.`; // 성공 피드백 메세지 전송
                setTimeout(() => {
                    feedback.textContent = ''
                }, 2000); // 피드백 메세지는 2초 후에 사라지도록 해둠.
                updateListItemRating(propertyId, newRatingInfo) // 외부에 보이는 평균 평범 업데이트
            }).catch(error => {
                console.error("평점 등록 실패:", error);
                alert('평점 등록에 실패했습니다.');
                feedback.textContent = '';
                updateStarsUI(parseInt(starContainer.dataset.rating)) // UI를 전송 실패 이전으로 되돌림.
            })
        }
    });
    // 별점 UI 제거.
    function updateStarsUI(rating) {
        stars.forEach(star => {
            star.classList.remove('hover'); // 모든 별에서 hover 효과를 제거한다.
            // 별의 데이터가 같거나, 같으면 그에 맞는 filled를 toggle합니다.
            star.classList.toggle('filled', star.dataset.value <= rating)
        })
    }
}
// 페이지 덧글용
function displayRepliesForPage(page) {
    // 현재 페이지 page 번호 업데이트(기본 :1)
    currentReplyPage = page;
    //
    const replyList = document.getElementById('reply-list');
    if (!replyList) return; // 댓글 목록 없으면 종료
    replyList.innerHTML = ''; // 기존 댓글 목록 비우기
    // 댓글이 없다면 작성된 댓글이 없습니다 띄우기
    if (allReplies.length === 0) {
        replyList.innerHTML = '<li>작성된 댓글이 없습니다.</li>';
        renderReplyPagination(); // 공백 기준으로 페이지네이션
        return // 함수 종료
    }
    // 현재 페이지에 표시할 댓글의 시작/끝 인덱스 계산
    const startIndex = (page - 1) * repliesPerPage,
        endIndex = startIndex + repliesPerPage,
        repliesToShow = allReplies.slice(startIndex, endIndex);

    // 현재 페이지의 댓글들을 HTML로 생성하여 추가
    repliesToShow.forEach(reply => {
        const li = document.createElement('li'); // 새 li 요소 추가
        li.className = 'reply-item'; // css 클래스 추가
        li.dataset.replyId = reply.id; // 댓글 고유 id를 dataset에 저장
        // 댓글 내용, 작성자, 시간, 수정, 삭제 추가
        li.innerHTML = ` <div class="reply-header"> <span class="user-id">${reply.userId}</span> <span class="timestamp">${new Date(reply.createdAt).toLocaleString()}</span> </div> <p class="reply-content">${reply.replyContent}</p> <div class="reply-actions"> <button class="edit-reply-btn">수정</button> <button class="delete-reply-btn">삭제</button> </div> `;
        replyList.appendChild(li) // 이후 댓글 목록에 추가
    });
    renderReplyPagination() // 댓글 목록 업데이트 이후 페이지네이션 다시 실행(한 페이지 5개)
}

function renderReplyPagination() {
    const paginationContainer = document.getElementById('reply-pagination');
    if (!paginationContainer) return; // 페이지네이션 할 컨테이너 없으면 함수 종료

    paginationContainer.innerHTML = ''; // 기존 페이지네이션 버튼 제거

    const totalPages = Math.ceil(allReplies.length / repliesPerPage); // 전체 페이지수 계산
    if (totalPages <= 1) return; // 전체 페이지가 1개 이하면 페이지네이션 실행 안 함.

    // 이전 버튼 생성 및 추가
    const prevBtn = document.createElement('button');
    prevBtn.className = 'page-btn';
    prevBtn.textContent = '이전';
    prevBtn.disabled = currentReplyPage === 1; // 현재 페이지가 1이라면 disable함
    prevBtn.addEventListener('click', () => displayRepliesForPage(currentReplyPage - 1));
    paginationContainer.appendChild(prevBtn);

    // 페이지 버튼 번호 생성 및 추가
    for (let i = 1; i <= totalPages; i++) {
        const pageBtn = document.createElement('button');
        pageBtn.className = 'page-btn';
        pageBtn.textContent = i;
        if (i === currentReplyPage) {
            pageBtn.classList.add('active') // 현재 페이지 버튼 활성화를 적용
        }
        // 클릭시 해당 페이지로 이동
        pageBtn.addEventListener('click', () => displayRepliesForPage(i));
        paginationContainer.appendChild(pageBtn)
    }

    // 다음 버튼 생성 및 추가
    const nextBtn = document.createElement('button');
    nextBtn.className = 'page-btn';
    nextBtn.textContent = '다음';
    nextBtn.disabled = currentReplyPage === totalPages; // 현재 페이지가 마지막(모든 페이지)면 disabled합니다.
    nextBtn.addEventListener('click', () => displayRepliesForPage(currentReplyPage + 1));
    paginationContainer.appendChild(nextBtn)
}

//댓대글 불러오기
function fetchReplies(propertyId, goToLastPage = !1) {
    allReplies = []; // 기존 allReplies를 초기화합니다.

    // 서버에서 api를 통해서 propertyId를 공유하는 덧글 데이터를 가져옵니다.
    fetch(`/api/replies?propertyId=${propertyId}`).then(response => {
        if (!response.ok) throw new Error('댓글 로딩 실패');
        return response.json()

    }).then(replies => {
        allReplies = replies; // 가져온 덧글 데이터를 allReplies 배열에 저장
        // 페이지에 따라서 댓글 표시
        if (goToLastPage) { // 새 덧글 작성 후 마지막 페이지로 이동할 때
            const totalPages = Math.ceil(allReplies.length / repliesPerPage);
            displayRepliesForPage(totalPages || 1) // 마지막 페이지 혹은 댓글이 없으면 1페이지
        } else { // 보통 첫 페이지부터 보여주는 경우
            displayRepliesForPage(1)
        }
    }).catch(error => { // 오류 발생시 코드 
        console.error(error);
        const replyList = document.getElementById('reply-list');
        if (replyList) replyList.innerHTML = '<li>댓글을 불러오는 데 실패했습니다.</li>'
    })
}

function setupReplySubmitListener() {
    // 모달 내에 덧글 submit 이벤트 리스너를 추가합니다.
    modalBody.addEventListener('submit', e => {
        // 이벤트가 발생한 대상이 댓글 폼(reply-form)인지 확인.
        if (e.target.id === 'reply-form') {
            const textarea = document.getElementById('reply-textarea'),
                content = textarea.value; // 댓글의 content의 value 가져오기.
            
            if (!content.trim()) { // 글 내용이 비었을 경우에는,
                alert('댓글 내용을 입력하세요.'); // 경고 메시지하고,
                return // 종료
            }
            
            const replyList = document.getElementById('reply-list'),
                propertyId = replyList.dataset.propertyId; // 해당 매물의 propertyId 가져오기
            
            // propertyId가 유효한지 확인
            if (!propertyId) {
                alert('댓글을 등록할 수 없습니다. 다시 시도해 주세요.');
                return
            }
            
            // 유효하다면 서버에 덧글 데이터를 전송합니다.
            fetch('/api/replies', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },

                // 댓글 내용과 propertyId(구분용)를 Json 형태로 전송
                body: JSON.stringify({ 
                    replyContent: content,
                    propertyId: propertyId
                })
            }).then(response => {
                if (!response.ok) throw new Error('댓글 작성에 실패했습니다.');
                textarea.value = ''; // 댓글 작성이 성공했다면 댓글 입력창을 비웁니다.
                fetchReplies(propertyId, !0) // 댓글 목록만을 새로고침한 후 마지막 페이지로 이동합니다.
            }).catch(error => alert(error.message)) // 오류시 알려줍니다.
        }
    });

    modalBody.addEventListener('click', e => {
        // 클릭된 요소가 reply-item 안에 있는지 확인합니다.
        const replyItem = e.target.closest('.reply-item');
        if (!replyItem) return; // 아닐 시 반환.

        // 현재 매물의 ID와 클릭된 댓글의 ID 가져오기(현재는 테스트용입니다).
        // 향후에는 userId를 기준으로 삭제하도록 할 것
        const propertyId = document.getElementById('reply-list').dataset.propertyId,
            replyId = replyItem.dataset.replyId;

        // 삭제 버튼 클릭 처리
        if (e.target.classList.contains('delete-reply-btn')) {
            // 사용자에게 확인 요청
            if (!confirm('정말 이 댓글을 삭제하시겠습니까?')) return;
            // 서버에 delete 요청
            fetch(`/api/replies/${replyId}`, {
                method: 'DELETE'
            }).then(response => {
                if (!response.ok) throw new Error('삭제 권한이 없거나, 서버 오류입니다.');
                // 성공시  댓글 목록 새로 고침
                fetchReplies(propertyId)
            }).catch(error => alert(error.message))
        }

        // 수정 버튼 클릭 처리
        if (e.target.classList.contains('edit-reply-btn')) {
            // 원본 댓글의 content 가져오기
            const originalContent = replyItem.querySelector('.reply-content').textContent,
                newContent = prompt('수정할 내용을 입력하세요:', originalContent);
            // 새 내용이 없거나, 이전과 같을시 종료
            if (!newContent || newContent.trim() === originalContent) return;
            fetch(`/api/replies/${replyId}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    replyContent: newContent
                })
            }).then(response => {
                if (!response.ok) throw new Error('수정 권한이 없거나, 서버 오류입니다.');
                // 성공시  댓글 목록 새로 고침
                fetchReplies(propertyId)
            }).catch(error => alert(error.message))
        }
    })
}

// 매물 이미지에 들어갈 이미지의 링크. src = 주소, thumb = 섬네일 주소
const images = [{
    src: 'https://cdn.pixabay.com/photo/2015/04/24/10/18/studio-737569_1280.jpg',
    thumb: 'https://cdn.pixabay.com/photo/2015/04/24/10/18/studio-737569_1280.jpg'
}, {
    src: 'https://cdn.pixabay.com/photo/2015/04/24/10/19/studio-737575_1280.jpg',
    thumb: 'https://cdn.pixabay.com/photo/2015/04/24/10/19/studio-737575_1280.jpg'
}, {
    src: 'https://cdn.pixabay.com/photo/2015/04/24/10/18/studio-737572_1280.jpg',
    thumb: 'https://cdn.pixabay.com/photo/2015/04/24/10/18/studio-737572_1280.jpg'
}, {
    src: 'https://cdn.pixabay.com/photo/2014/03/06/11/26/officetel-280748_1280.jpg',
    thumb: 'https://cdn.pixabay.com/photo/2014/03/06/11/26/officetel-280748_1280.jpg'
}];

// 기본 인덱스 = 0
let currentIndex = 0;
// 들어간 이미지들의 갯수를 셉니다.
const totalImages = images.length;
let fixedImageWidth = null;
let fixedImageHeight = null;

function createThumbnails() {
    const thumbnailGallery = document.querySelector('.thumbnail-scroll-wrapper');
    if (!thumbnailGallery) return; // 썸네일 갤러리 컨테이너가 없으면 함수 종료

    thumbnailGallery.innerHTML = ''; // 기존 썸네일 목록을 모두 비움 (초기화)

    // `images` 배열의 각 이미지 데이터(`imgData`)와 인덱스(`index`)에 대해 반복
    images.forEach((imgData, index) => {
        const thumb = document.createElement('img'); // 새 <img> 요소를 생성
        thumb.className = 'thumbnail'; // CSS 스타일링을 위한 클래스 추가
        thumb.src = imgData.thumb; // 썸네일 이미지 소스 설정
        thumb.alt = `Thumbnail ${index + 1}`; // 이미지 대체 텍스트 설정
        thumb.dataset.index = index; // 썸네일의 인덱스를 데이터 속성으로 저장 (나중에 활용)

        thumbnailGallery.appendChild(thumb); // 썸네일 갤러리 컨테이너에 썸네일 이미지 추가

        // 썸네일 클릭 시 해당 이미지를 메인 뷰어에 표시하도록 이벤트 리스너 추가
        thumb.addEventListener('click', () => {
            showImage(index) // showImage 함수를 호출하여 클릭된 썸네일에 해당하는 이미지를 보여줌
        })
    })
}

function showImage(index) {
    if (totalImages === 0) return; // 전체 이미지가 없으면 함수 종료 (오류 방지)

    const currentImageElement = document.getElementById('current-image'), // 메인 이미지 뷰어의 <img> 태그
        currentImageIndexSpan = document.getElementById('current-image-index'); // 현재 이미지 번호를 표시하는 <span>

    if (!currentImageElement || !currentImageIndexSpan) return; // 필요한 요소가 없으면 함수 종료

    // 인덱스 값 유효성 검사 및 순환 로직 (예: 마지막 이미지에서 다음으로 넘기면 첫 이미지로)
    currentIndex = index < 0 ? totalImages - 1 : index >= totalImages ? 0 : index;

    // 이미지가 고정된 너비/높이 값으로 설정되어 있다면 적용
    if (fixedImageWidth && fixedImageHeight) {
        currentImageElement.style.width = `${fixedImageWidth}px`;
        currentImageElement.style.height = `${fixedImageHeight}px`
    }

    // 부드러운 이미지 전환 효과 (페이드 아웃 -> 소스 변경 -> 페이드 인)
    currentImageElement.style.opacity = '0'; // 이미지를 투명하게 만들어 페이드 아웃 효과
    setTimeout(() => {
        currentImageElement.src = images[currentIndex].src; // 메인 이미지 소스를 새 이미지로 변경
        currentImageElement.style.opacity = '1'; // 다시 불투명하게 만들어 페이드 인 효과
    }, 150); // 150ms 후 이미지 변경 및 페이드 인 시작

    currentImageIndexSpan.textContent = currentIndex + 1; // 현재 이미지 번호 업데이트 (1부터 시작)

    // 모든 썸네일을 순회하며 현재 활성화된 썸네일을 시각적으로 표시
    document.querySelectorAll('.thumbnail').forEach((thumb, idx) => {
        if (idx === currentIndex) { // 현재 이미지의 썸네일인 경우
            thumb.classList.add('active'); // 'active' 클래스 추가하여 강조
            // 썸네일이 화면에 보이도록 스크롤 (필요할 경우)
            thumb.scrollIntoView({
                behavior: 'smooth', // 부드러운 스크롤
                block: 'nearest',   // 뷰포트에 가장 가깝게
                inline: 'center'    // 수평 중앙에 오도록
            });
        } else {
            thumb.classList.remove('active'); // 다른 썸네일은 'active' 클래스 제거
        }
    })
}

function initGallery() {
    const galleryContainer = document.querySelector('.image-gallery-container');

    // 이미지가 없으면 갤러리 컨테이너를 숨기고 함수 종료
    if (images.length === 0) {
        if (galleryContainer) galleryContainer.style.display = 'none';
        return
    }

    // 이미지가 있다면 갤러리 컨테이너를 표시 (기본적으로 숨겨져 있을 경우)
    if (galleryContainer) galleryContainer.style.display = 'flex';

    const prevArrow = document.querySelector('.prev-arrow'), // '이전' 버튼
        nextArrow = document.querySelector('.next-arrow'), // '다음' 버튼
        totalImagesSpan = document.getElementById('total-images'), // 전체 이미지 수를 표시하는 <span>
        mainImageViewer = document.querySelector('.main-image-viewer'); // 메인 이미지 뷰어 컨테이너

    // 필수 요소 중 하나라도 없으면 함수 종료 (오류 방지)
    if (!prevArrow || !nextArrow || !totalImagesSpan || !mainImageViewer) return;

    createThumbnails(); // 썸네일들을 생성

    totalImagesSpan.textContent = totalImages; // 전체 이미지 수 표시

    // '이전' 버튼 이벤트 리스너 재설정 (클론 후 교체하여 이벤트 중복 방지)
    const newPrevArrow = prevArrow.cloneNode(true); // 버튼 복제 (이벤트 리스너는 복제되지 않음)
    prevArrow.parentNode.replaceChild(newPrevArrow, prevArrow); // 기존 버튼을 복제된 버튼으로 교체
    newPrevArrow.addEventListener('click', () => showImage(currentIndex - 1)); // 새로운 이벤트 리스너 추가

    // '다음' 버튼 이벤트 리스너 재설정 (위와 동일한 이유)
    const newNextArrow = nextArrow.cloneNode(true);
    nextArrow.parentNode.replaceChild(newNextArrow, nextArrow);
    newNextArrow.addEventListener('click', () => showImage(currentIndex + 1));

    // 첫 이미지 로드 및 메인 뷰어 크기 조정
    const firstImage = new Image(); // 새 Image 객체 생성 (DOM에 추가되지 않음)
    firstImage.onload = function () { // 첫 이미지가 로드 완료되면 실행
        const viewerWidth = mainImageViewer.clientWidth, // 뷰어의 현재 너비
            viewerMaxHeight = 400, // 뷰어의 최대 높이 (하드코딩)
            ratio = this.naturalWidth / this.naturalHeight; // 이미지의 원본 가로/세로 비율

        let newHeight = viewerWidth / ratio, // 뷰어 너비에 맞춘 새로운 높이 계산
            newWidth = viewerWidth; // 뷰어 너비에 맞춘 새로운 너비

        // 계산된 높이가 최대 높이를 초과하면 최대 높이에 맞추고 너비 재계산
        if (newHeight > viewerMaxHeight) {
            newHeight = viewerMaxHeight;
            newWidth = viewerMaxHeight * ratio;
        }

        fixedImageWidth = newWidth; // 계산된 고정 너비 저장
        fixedImageHeight = newHeight; // 계산된 고정 높이 저장

        showImage(0); // 갤러리 초기 이미지 (첫 번째 이미지) 표시
    };
    firstImage.src = images[0].src; // 첫 이미지의 소스를 설정하여 로드 시작



}