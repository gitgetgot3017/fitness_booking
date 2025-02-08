import {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import axios from "axios";

function Login() {

    let [memberNum, setMemberNum] = useState("");
    let [password, setPassword] = useState("");

    let navigate = useNavigate();

    useEffect(() => {
        axios.post("/members/login", {
                memberNum: "2073", // TODO: 하드 코딩이 아닌 실제 데이터로 변경해야 함
                password: "060820"
            }, {
                headers: { "Content-Type": "application/json" }
            })
            .then((result) => {
                window.localStorage.setItem("accessToken", result.data.accessToken);
                window.localStorage.setItem("refreshToken", result.data.refreshToken);

                navigate("/");
            })
            .catch((error) => {
                console.error("로그인 중 에러 발생:", error.response ? error.response.data : error.message);
                if (error.response) {
                    console.error("에러 상태 코드:", error.response.status);
                }
            });
    }, []);

    return (
        <div>안녕</div>
    );
}

export default Login;