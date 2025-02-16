import './App.css';
import { Routes, Route } from 'react-router-dom';
import CourseMain from './pages/course/CourseMain';
import CourseDetail from "./pages/course/CourseDetail";
import Login from "./pages/member/Login";
import RegisterCourse from "./pages/admin/RegisterCourse";
import Notification from "./pages/notification/Notification";

function App() {
  return (
    <div className="App">
      <Routes>
        <Route path="/courses" element={<CourseMain></CourseMain>}></Route>
        <Route path="/courses/detail" element={<CourseDetail></CourseDetail>}></Route>

        <Route path="/members/login" element={<Login></Login>}></Route>

        <Route path="/admin/register/courses" element={<RegisterCourse></RegisterCourse>}></Route>
        <Route path="/notifications" element={<Notification></Notification>}></Route>
      </Routes>
    </div>
  );
}

export default App;
