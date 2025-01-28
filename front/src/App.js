import './App.css';
import { Routes, Route } from 'react-router-dom';
import CourseList from './pages/course/CourseList';

function App() {
  return (
    <div className="App">
      <Routes>
        <Route path="/courses" element={<CourseList></CourseList>}></Route>
      </Routes>
    </div>
  );
}

export default App;
