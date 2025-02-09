import './CourseMain.css';
import {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import axios from "axios";

function CourseMain() {

    let [memberName, setMemberName] = useState('');
    let [memberNum, setMemberNum] = useState('');
    let [subscriptionName, setSubscriptionName] = useState('');
    let [startDate, setStartDate] = useState('');
    let [endDate, setEndDate] = useState('');
    let [completedCount, setCompletedCount] = useState(0);
    let [availableCount, setAvailableCount] = useState(0);
    let [reservedCount, setReservedCount] = useState(0);
    let [courses, setCourses] = useState([]);
    let [courseMainHistoryList, setCourseMainHistoryList] = useState([]);
    let [date, setDate] = useState(new Date());

    let navigate = useNavigate ();

    let accessToken = localStorage.getItem("accessToken");

    useEffect(() => {
        axios.get("/courses", {
                params: {date: date.toISOString().slice(0, 10)},
                ...(accessToken && {headers: {Authorization: `Bearer ${accessToken}`}})
            })
            .then((result) => {
                setMemberName(result.data.memberName);
                setMemberNum(result.data.memberNum);
                setSubscriptionName(result.data.subscriptionName);
                setStartDate(result.data.startDate);
                setEndDate(result.data.endDate);
                setCompletedCount(result.data.completedCount);
                setAvailableCount(result.data.availableCount);
                setReservedCount(result.data.reservedCount);
                setCourseMainHistoryList(result.data.courseMainHistoryList);
                setCourses(result.data.courses);
            })
            .catch((error) => {
                console.error("메인 정보 가져오는 중 에러 발생:", error.response ? error.response.data : error.message);
                if (error.response) {
                    console.error("에러 상태 코드:", error.response.status);
                }

                if (error.response.status == 401) {
                    if (error.response.data.error == "ACCESS_TOKEN_EXPIRED") {
                        axios.patch("/refresh/token",
                            {
                                refreshToken: localStorage.getItem("refreshToken") },
                            { ...(accessToken && { headers: { Authorization: `Bearer ${accessToken}` } })
                            }, {
                                headers: { "Content-Type": "application/json" }
                            })
                            .then((result) => {
                                window.localStorage.setItem("accessToken", result.data.accessToken);
                                window.localStorage.setItem("refreshToken", result.data.refreshToken);
                            })
                            .catch((error) => {
                                console.error("토큰 갱신 요청 중 에러 발생:", error.response ? error.response.data : error.message);
                                if (error.response) {
                                    console.error("에러 상태 코드:", error.response.status);
                                }

                                if (error.response.status == 401) {
                                    navigate("/members/login");
                                }
                            });
                    } else if (error.response.data.error == "ACCESS_TOKEN_INVALID") {
                        navigate("/members/login");
                    }
                }
            });
    }, []);

    function storeCourseDate(currentDay) {
        let yyyy = date.getFullYear()
        let mm = String(date.getMonth() + 1).padStart(2, "0");
        let dd = String(currentDay).padStart(2, "0");

        let formattedDate = `${yyyy}-${mm}-${dd}`;
        window.localStorage.setItem("courseDate", formattedDate);
    }

    return (
        <div className="container">
            <header className="header">
                <div className="menu">
                    <h2>홈 &gt; 그룹예약</h2>
                    <div className="hamburger">☰</div>
                </div>
                <hr />
                <div className="user-info">
                    <p><strong>{memberName}({memberNum})</strong> 회원님</p>
                </div>
            </header>

            <div className="membership-box">
                <div className="left">
                    <p><strong>{subscriptionName}</strong></p>
                    <p>{startDate} ~ {endDate}</p>
                </div>
                <div className="right">
                    <p><strong>{completedCount} / {availableCount}</strong></p>
                    <p>(예약 {reservedCount}건)</p>
                </div>
            </div>

            <div className="calendar">
                <div className="calendar-header">
                    <button className="calendar-button" onClick={() => setDate(new Date(date.getFullYear(), date.getMonth() - 1))}>&lt;</button>
                    <h2>{date.getFullYear()}.{date.getMonth() + 1}</h2>
                    <button className="calendar-button" onClick={() => setDate(new Date(date.getFullYear(), date.getMonth() + 1))}>&gt;</button>
                </div>
                <div className="calendar-grid">
                    <div className="weekday">Sun</div>
                    <div className="weekday">Mon</div>
                    <div className="weekday">Tue</div>
                    <div className="weekday">Wed</div>
                    <div className="weekday">Thu</div>
                    <div className="weekday">Fri</div>
                    <div className="weekday">Sat</div>

                    {Array.from({ length: 42 }, (_, i) => {
                        let firstDateOfMonth = new Date(date.getFullYear(), date.getMonth(), 1); // 첫째 날
                        let firstDay = firstDateOfMonth.getDay(); // 첫째 날의 요일
                        let daysInMonth = new Date(date.getFullYear(), date.getMonth() + 1, 0).getDate(); // 해당 달의 날짜 수

                        let currentDay = i - firstDay + 1; // 현재 날짜 계산
                        if (currentDay <= 0 || currentDay > daysInMonth) {
                            return <div key={i} className="day empty"></div>;
                        }
                        if (courseMainHistoryList.includes(currentDay)) {
                            return <div key={i} className="day registered" onClick={() => {storeCourseDate(currentDay)}}>{currentDay}</div>;
                        }
                        return <div key={i} className="day" onClick={() => {storeCourseDate(currentDay)}}>{currentDay}</div>;
                    })}
                </div>
            </div>

            <div className="tabs">
                <button className="tab active" data-tab="available">예약가능</button>
                <button className="tab" data-tab="completed">예약완료</button>
            </div>

            <div className="tab-content" id="available">
                <div className="time-group">
                    {/* 임시 코드 */}
                    <div className="reservation-item" style={{marginBottom: "20px"}} onClick={() => {
                        window.localStorage.setItem("courseId", 62);
                        navigate("/courses/detail");
                    }}>
                        <div className="left" style={{display: "flex", alignItems: "center"}}>
                            <img style={{marginRight: "10px"}} />
                            <div>
                                <p><strong>지수(캐딜락)</strong></p>
                                <p>22:00 - 22:50</p>
                            </div>
                        </div>
                        <div className="right">
                            <h2>5 / 6</h2>
                        </div>
                    </div>
                    <div className="reservation-item" style={{marginBottom: "20px"}} onClick={() => {
                        window.localStorage.setItem("courseId", 2);
                        navigate("/courses/detail");
                    }}>
                        <div className="left" style={{display: "flex", alignItems: "center"}}>
                            <img style={{marginRight: "10px"}} />
                            <div>
                                <p><strong>지수(캐딜락)</strong></p>
                                <p>23:00 - 23:50</p>
                            </div>
                        </div>
                        <div className="right">
                            <h2>6 / 6</h2>
                        </div>
                    </div>
                    {/* 임시 코드 */}
                    {
                        courses.map(function(course, i) {
                            return (
                                <div className="reservation-item" key={i} style={{marginBottom: "20px"}} onClick={() => {
                                    window.localStorage.setItem("courseId", course.courseId);
                                    navigate("/courses/detail");
                                }}>
                                    <div className="left" style={{display: "flex", alignItems: "center"}}>
                                        <img style={{marginRight: "10px"}} />
                                        <div>
                                            <p><strong>{course.instructorName}({course.courseName})</strong></p>
                                            <p>{course.courseStartTime} - {course.courseEndTime}</p>
                                        </div>
                                    </div>
                                    <div className="right">
                                        <h2>{course.attendeeCount} / 6</h2>
                                    </div>
                                </div>
                            );
                        })
                    }
                </div>
            </div>
        </div>
    );
}

export default CourseMain;