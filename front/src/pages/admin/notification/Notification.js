import './Notification.css';
import {useEffect, useState} from "react";
import api from "../../../api";

function Notification() {

    let [notifications, setNotifications] = useState([]);

    useEffect(() => {
        api.get("/api/notifications/history")
            .then((result) => {
                setNotifications(result.data);
            })
            .catch((error) => {
                console.error("알림 정보 가져오는 중 에러 발생:", error.response ? error.response.data : error.message);
            });
    }, [])

    return (
        <div className="container">
            <div className="header">
                <div className="menu">
                    <h2>알림</h2>
                    <div className="hamburger">☰</div>
                </div>
                <hr />
            </div>
            <div className="notifications">
                {
                    notifications.map(function(notification) {
                        return (
                            <div className="notification-item">
                                <div className="left">{notification.content}</div>
                                <div className="right">{notification.notificationDateTime}</div>
                            </div>
                        );
                    })
                }
                <div className="notification-item">
                    <div className="left">이현지(2073) 회원님의 수강권이 3회 남았습니다.</div>
                    <div className="right">2024-02-28</div>
                </div>
                <div className="notification-item">
                    <div className="left">김민수(3051) 회원님의 수강권이 2회 남았습니다.</div>
                    <div className="right">2024-02-28</div>
                </div>
                <div className="notification-item read">
                    <div className="left">박서연(1098) 회원님의 수강권이 1회 남았습니다.</div>
                    <div className="right">2024-02-27</div>
                </div>
            </div>
        </div>
    );
}

export default Notification;