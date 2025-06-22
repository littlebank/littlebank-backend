const token = localStorage.getItem("accessToken");
if (!token) {
    alert("로그인이 필요합니다.");
    window.location.href = "/view/public/login";
}

async function logout() {
    const confirmed = confirm("정말 로그아웃하시겠습니까?");
    if (!confirmed) return;

    try {
        const res = await fetch("/api-user/auth/logout", {
            method: "POST",
            headers: {
                "Authorization": `Bearer ${token}`
            },
            credentials: "include"
        });

        if (res.status === 204 || res.status === 200) {
            localStorage.removeItem("accessToken");
            window.location.href = "/view/public/login";
        } else if (res.status === 401 || res.status === 403) {
            localStorage.removeItem("accessToken");
            alert("세션이 만료되어 자동 로그아웃됩니다.");
            window.location.href = "/view/public/login";
        } else {
            alert("로그아웃 실패");
        }
    } catch (err) {
        alert("에러 발생: " + err.message);
    }
}