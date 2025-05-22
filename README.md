
# 🔍 공급망 실사 응답 처리 서비스

이 서비스는 기업의 ESG 공급망 실사 대응을 위한 핵심 컴포넌트로,  
협력사의 실사 항목에 대한 응답을 수집하고 위반 여부를 분석하여 결과를 반환합니다.

---

## ✅ 주요 기능 요약

| 기능 분류 | 설명 |
|-----------|------|
| 응답 제출 | 협력사 또는 담당자가 공급망 실사 항목에 대해 응답 제출 |
| 위반 분석 | 제출된 응답을 기반으로 내부 기준에 따라 위반 항목 도출 |
| 응답 수정 | 응답자가 기존 응답을 수정할 수 있으며, 소유자 확인 필수 |
| 결과 조회 | 이미 제출된 위반 결과를 사용자에게 제공 |

---

## 🔐 인증 구조

- 모든 요청은 공통적으로 `X-MEMBER-ID` 기반 인증을 요구합니다.
- 인증 실패 시 `401 Unauthorized`, 소유자 불일치 시 `403 Forbidden` 처리됩니다.

---

## 🔄 응답 처리 흐름도

```mermaid
flowchart TD

%% ───── 공통: 인증 블록 ─────
    Start((Start))
    Start --> Auth["X-MEMBER-ID 인증"]
    Auth --> AuthCheck{"인증 성공 여부"}
    AuthCheck -- "아니오" --> Error401["401 Unauthorized"] --> End1((End))
    AuthCheck -- "예" --> Router["인증 후 요청 분기"]

%% ───── 응답 제출 흐름 ─────
    Router --> PostReq["응답 제출 요청 (POST)"]
    PostReq --> SaveAns["응답 저장 및 위반 ID 추출"]
    SaveAns --> FindViol["위반 항목 조회"]
    FindViol --> PostResp["분석 결과 반환"]
    PostResp --> End2((End))

%% ───── 응답 수정 흐름 ─────
    Router --> PutReq["응답 수정 요청 (PUT)"]
    PutReq --> CheckOwner{"소유자 확인"}
    CheckOwner -- "아니오" --> Error403["403 Forbidden"] --> End3((End))
    CheckOwner -- "예" --> DeleteOld["기존 응답 삭제"]
    DeleteOld --> SaveNew["응답 재저장 및 위반 ID 추출"]
    SaveNew --> FindViol2["위반 항목 조회"]
    FindViol2 --> PutResp["분석 결과 반환"]
    PutResp --> End4((End))

%% ───── 결과 조회 흐름 ─────
    Router --> GetReq["결과 조회 요청 (GET)"]
    GetReq --> LoadStored["저장된 위반 결과 불러오기"]
    LoadStored --> GetResp["분석 결과 반환"]
    GetResp --> End5((End))

%% ───── 스타일 정의 ─────
    classDef forest fill:#e6f4ea,stroke:#2e7d32,stroke-width:1.5px,color:#2e7d32;
    classDef terminal fill:#d0f0c0,stroke:#1b5e20,color:#1b5e20;
    classDef error fill:#fdecea,stroke:#c62828,color:#c62828;

%% ───── 클래스 적용 ─────
    class Start,End1,End2,End3,End4,End5 terminal;
    class Auth,AuthCheck,Router,PostReq,SaveAns,FindViol,PostResp,PutReq,CheckOwner,DeleteOld,SaveNew,FindViol2,PutResp,GetReq,LoadStored,GetResp forest;
    class Error401,Error403 error;
````

---

## 🧠 로직 설명

* ✅ **응답 제출 (POST)**
  사용자가 실사 항목에 대해 응답을 제출하면, 서버는 이를 저장하고 해당 응답 내 위반 여부를 판별한 뒤 결과를 반환합니다.

* 🛠 **응답 수정 (PUT)**
  응답을 수정하기 위해서는 소유자 확인이 필요하며, 기존 응답을 삭제하고 새로운 응답으로 대체됩니다. 이후 위반 여부를 다시 분석합니다.

* 📥 **결과 조회 (GET)**
  기존에 저장된 분석 결과를 불러와 사용자에게 제공합니다.

---

## 📌 사용 기술

* **Spring Boot REST API**
* **JWT 기반 인증 → Gateway에서 `X-MEMBER-ID` 헤더로 전달**
* **위반 분석 로직**은 별도 서비스로 분리되어 관리
* **ESG 공급망 실사 항목**과의 연계 구조 설계

---

## 📈 확장 가능성

* 실시간 경고 (Slack, Email) 연동
* ESG 리스크 등급 자동 산출 로직과 통합
* 위반 응답에 대한 이력 관리 및 감사 로그 기능 추가 예정


