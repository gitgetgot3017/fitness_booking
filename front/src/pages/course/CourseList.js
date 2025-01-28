import './CourseList.css';
import {useEffect, useState} from "react";
import axios from "axios";

function CourseList() {

    let [memberName, setMemberName] = useState('');
    let [memberNum, setMemberNum] = useState('');
    let [subscriptionName, setSubscriptionName] = useState('');
    let [startDate, setStartDate] = useState('');
    let [endDate, setEndDate] = useState('');
    let [completedCount, setCompletedCount] = useState(0);
    let [availableCount, setAvailableCount] = useState(0);
    let [reservedCount, setReservedCount] = useState(0);
    let [courses, setCourses] = useState([]);

    useEffect(() => {
        axios.get("/courses?year=2025&month=1&week=1&dayOfWeek=TUES")
            .then((result) => {
                setMemberName(result.data.memberName);
                setMemberNum(result.data.memberNum);
                setSubscriptionName(result.data.subscriptionName);
                setStartDate(result.data.startDate);
                setEndDate(result.data.endDate);
                setCompletedCount(result.data.completedCount);
                setAvailableCount(result.data.availableCount);
                setReservedCount(result.data.reservedCount);
                setCourses(result.data.courses);
            })
            .catch((error) => {
                console.error("메인 정보 가져오는 중 에러 발생:", error.response ? error.response.data : error.message);
                if (error.response) {
                    console.error("에러 상태 코드:", error.response.status);
                }
            });
    }, []);

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
                    <button>&lt;</button>
                    <h2>2025.01</h2>
                    <button>&gt;</button>
                </div>
                <div className="calendar-grid">
                    <div className="weekday">Sun</div>
                    <div className="weekday">Mon</div>
                    <div className="weekday">Tue</div>
                    <div className="weekday">Wed</div>
                    <div className="weekday">Thu</div>
                    <div className="weekday">Fri</div>
                    <div className="weekday">Sat</div>

                    <div className="day empty"></div>
                    <div className="day empty"></div>
                    <div className="day empty"></div>
                    <div className="day">1</div>
                    <div className="day">2</div>
                    <div className="day">3</div>
                    <div className="day">4</div>
                    <div className="day">5</div>
                    <div className="day">6</div>
                    <div className="day">7</div>
                    <div className="day">8</div>
                    <div className="day">9</div>
                    <div className="day">10</div>
                    <div className="day">11</div>
                    <div className="day">12</div>
                    <div className="day">13</div>
                    <div className="day">14</div>
                    <div className="day">15</div>
                    <div className="day">16</div>
                    <div className="day">17</div>
                    <div className="day">18</div>
                    <div className="day">19</div>
                    <div className="day">20</div>
                    <div className="day">21</div>
                    <div className="day">22</div>
                    <div className="day">23</div>
                    <div className="day">24</div>
                    <div className="day">25</div>
                    <div className="day">26</div>
                    <div className="day">27</div>
                    <div className="day">28</div>
                    <div className="day">29</div>
                    <div className="day">30</div>
                    <div className="day">31</div>
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
                                <div className="reservation-item" key={i} style={{marginBottom: "20px"}}>
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

export default CourseList;