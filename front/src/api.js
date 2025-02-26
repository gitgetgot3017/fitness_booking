import axios from 'axios';

// axios 인스턴스 생성
const api = axios.create();

// 요청 인터셉터
api.interceptors.request.use(
    config => {
        const accessToken = localStorage.getItem("accessToken");
        config.headers["Authorization"] = `Bearer ${accessToken}`;
        return config;
    },
    error => {
        return Promise.reject(error);
    }
);

// 응답 인터셉터
api.interceptors.response.use(
    response => {
        return response;
    },
    error => {
        if (error.response) {
            console.error("에러 상태 코드:", error.response.status);

            if (error.response.status == 401) {
                if (error.response.data.error === "ACCESS_TOKEN_EXPIRED") {
                    api.patch(
                        "/api/refresh/token",
                        {refreshToken: localStorage.getItem("refreshToken")},
                        {headers: {"Content-Type": "application/json"}}
                    ).then((result) => {
                        localStorage.setItem("accessToken", result.data.accessToken);
                        localStorage.setItem("refreshToken", result.data.refreshToken);
                    }).catch((error) => {
                        console.error("토큰 갱신 요청 중 에러 발생:", error.response ? error.response.data : error.message);
                        if (error.response) {
                            console.error("에러 상태 코드:", error.response.status);

                            if (error.response.status == 401) {
                                window.location.href = "/members/login";
                            }
                        }
                    });
                } else if (error.response.data.error === "ACCESS_TOKEN_INVALID") {
                    window.location.href = "/members/login";
                }
            }
        }
        return Promise.reject(error);
    }
);

export default api;