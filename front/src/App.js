import './App.css';
import { Routes, Route } from 'react-router-dom';
import CourseMain from './pages/course/CourseMain';
import CourseDetail from "./pages/course/CourseDetail";

function App() {
  return (
    <div className="App">
      <Routes>
        <Route path="/courses" element={<CourseMain></CourseMain>}></Route>
        <Route path="/courses/detail" element={<CourseDetail></CourseDetail>}></Route>
      </Routes>
    </div>
  );
}

export default App;
