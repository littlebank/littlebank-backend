const token = localStorage.getItem("accessToken");
if (!token) {
    alert("로그인이 필요합니다.");
    window.location.href = "/view/public/login";
}

async function fetchUserInfo() {
    try {
        const res = await fetch("/api-user/user/info", {
            headers: {
                "Authorization": `Bearer ${token}`
            }
        });

        if (res.ok) {
            const data = await res.json();
            document.getElementById("name").textContent = data.name;
            document.getElementById("email").textContent = data.email;
            document.getElementById("phone").textContent = data.phone;
            document.getElementById("statusMessage").value = data.statusMessage || "";
        } else if (res.status === 401 || res.status === 403) {
            alert("세션이 만료되었거나 유효하지 않습니다. 다시 로그인해주세요.");
            localStorage.removeItem("accessToken");
            window.location.href = "/view/public/login";
        } else {
            alert("회원 정보를 불러오지 못했습니다.");
        }
    } catch (err) {
        alert("에러 발생: " + err.message);
    }
}

async function updateStatusMessage() {
    const message = document.getElementById("statusMessage").value;
    try {
        const res = await fetch("/api-user/user/status-message", {
            method: "PATCH",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`
            },
            body: JSON.stringify({ statusMessage: message })
        });
        if (res.ok) {
            alert("상태 메시지가 업데이트되었습니다.");
        } else {
            alert("업데이트 실패");
        }
    } catch (err) {
        alert("에러 발생: " + err.message);
    }
}

async function deleteAccount() {
    const confirmed = confirm("정말로 계정을 삭제하시겠습니까?");
    if (!confirmed) return;

    try {
        const res = await fetch("/api-user/user", {
            method: "DELETE",
            headers: {
                "Authorization": `Bearer ${token}`
            }
        });

        if (res.status === 204) {
            alert("계정이 삭제되었습니다.");
            localStorage.removeItem("accessToken");
            window.location.href = "/view/public/login";
        } else {
            alert("계정 삭제 실패");
        }
    } catch (err) {
        alert("에러 발생: " + err.message);
    }
}

window.onload = () => {
    fetchUserInfo();
}