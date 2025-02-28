import './CourseMain.css';
import {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import api from "../../api";

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
    let [sidebarOpen, setSidebarOpen] = useState(false);

    let navigate = useNavigate();

    useEffect(() => {
        api.get("/api/courses", {
                params: {date: date.toISOString().slice(0, 10)}
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
                    <div className="hamburger" onClick={() => setSidebarOpen(true)}>☰</div>
                </div>
                <hr />
                <div className="user-info">
                    <p><strong>{memberName}({memberNum})</strong> 회원님</p>
                </div>
            </header>

            {/* Sidebar */}
            <div className={`sidebar ${sidebarOpen ? "open" : ""}`}>
                <button className="close-btn" onClick={() => setSidebarOpen(false)}>×</button>
                <ul>
                    <li><a onClick={() => navigate("/courses/recommendation")}>수업 추천</a></li>
                    <li><a onClick={() => navigate("/admin/register/courses")}>수업 등록</a></li>
                    <li><a onClick={() => navigate("/admin/register/instructors")}>강사 등록</a></li>
                    <li><a onClick={() => navigate("/notifications")}>알림</a></li>
                </ul>
            </div>

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
                    {
                        courses.map(function(course, i) {
                            return (
                                <div className="reservation-item" key={i} style={{marginBottom: "20px"}} onClick={() => {
                                    window.localStorage.setItem("courseId", course.courseId);
                                    navigate("/courses/detail");
                                }}>
                                    <div className="left" style={{display: "flex", alignItems: "center"}}>
                                        <img src={course.instructorImgUrl} style={{marginRight: "10px", width: "60px", height: "60px"}} />
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