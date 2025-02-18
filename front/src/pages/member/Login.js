import {useState} from "react";
import {useNavigate} from "react-router-dom";
import axios from "axios";
import api from "../../api";

function Login() {

    let [memberNum, setMemberNum] = useState("");
    let [password, setPassword] = useState("");

    let navigate = useNavigate();

    return (
        <>
            <input type="text" onChange={(e) => setMemberNum(e.target.value)} />
            <input type="password" onChange={(e) => setPassword(e.target.value)} />
            <button onClick={() => {
                axios.post("/members/login", {
                        memberNum: memberNum,
                        password: password
                    }, {
                        headers: { "Content-Type": "application/json" }
                    })
                    .then((result) => {
                        window.localStorage.setItem("accessToken", result.data.accessToken);
                        window.localStorage.setItem("refreshToken", result.data.refreshToken);

                        navigate("/"); // TODO: SSE 연결 요청이 잘 이뤄지는지, 연결이 잘 되는지 확인 필요함

                        if (result.data.grade === "ADMIN") {
                            api.get("/notifications")
                                .catch((error) => {
                                    console.error("SSE 연결 요청 중 에러 발생:", error.response ? error.response.data : error.message);
                                });
                        }
                    })
                    .catch((error) => {
                        console.error("로그인 중 에러 발생:", error.response ? error.response.data : error.message);
                        if (error.response) {
                            console.error("에러 상태 코드:", error.response.status);
                        }
                    })
            }}>로그인</button>
        </>
    );
}

export default Login;