import api from "./api";

// 캘린더 일정 월별 조회
export const fetchCalendarData = (month) => {
  return api.get(`/assignments/summary`, {
    params: { month: month },
  });
};

// 캘린더 일정 일별 조회
export const fetchTodoData = (month, day) => {
  return api.get(`/assignments`, {
    params: { month: month, day: day },
  });
};

// 내 출퇴근 정보 조회
export const fetchAttendanceData = (month, day) => {
  return api.get(`/attendances/me`, {
    params: { month: month, day: day },
  });
};

// 일정 추가
export const addCalendarEvent = (
  assignmentName,
  assignmentDate,
  assignmentType,
  description,
  token
) => {
  return api.post("/assignments", {
    name: assignmentName,
    date: assignmentDate,
    type: assignmentType,
    description: description,
    completed: false,
  });
};

// 일정 완료
export const checkEvent = (assignmentId) => {
  return api.patch(`/assignments/${assignmentId}`);
};
