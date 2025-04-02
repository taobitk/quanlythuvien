<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Mượn Sách</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
</head>
<body>

<div class="container mt-4">
  <div class="row justify-content-center">
    <div class="col-md-8 col-lg-6">
      <div class="card shadow-sm">
        <div class="card-header bg-primary text-white">
          <h1 class="card-title text-center h4 mb-0">Thông Tin Mượn Sách</h1>
        </div>
        <div class="card-body">
          <c:if test="${not empty errors.form}">
            <div class="alert alert-danger" role="alert">
              <c:out value="${errors.form}" />
            </div>
          </c:if>

          <form method="POST" action="${pageContext.request.contextPath}/muon-sach" class="needs-validation" novalidate>
            <input type="hidden" name="maSach" value="${sach.maSach}">

            <div class="mb-3">
              <label for="tenSach" class="form-label">Tên Sách:</label>
              <input type="text" id="tenSach" class="form-control" value="<c:out value='${sach.tenSach}'/>" readonly>
            </div>

            <div class="mb-3">
              <label for="maHocSinh" class="form-label">Tên Học Sinh:</label>
              <select id="maHocSinh" name="maHocSinh" class="form-select ${not empty errors.maHocSinh ? 'is-invalid' : ''}" required>
                <option value="">-- Chọn học sinh --</option>
                <c:forEach items="${hocSinhList}" var="hs">
                  <option value="${hs.maHocSinh}" ${userInput.maHocSinh == hs.maHocSinh ? 'selected' : ''}>
                    <c:out value="${hs.hoTen}"/> (<c:out value="${hs.lop}"/>)
                  </option>
                </c:forEach>
              </select>
              <c:if test="${not empty errors.maHocSinh}">
                <div class="invalid-feedback">
                  <c:out value="${errors.maHocSinh}" />
                </div>
              </c:if>
              <div class="invalid-feedback">Vui lòng chọn học sinh.</div>
            </div>

            <div class="mb-3">
              <label for="ngayMuon" class="form-label">Ngày Mượn:</label>
              <input type="text" id="ngayMuon" class="form-control" value="<c:out value='${ngayMuon}'/>" readonly>
            </div>

            <div class="mb-3">
              <label for="ngayTraInput" class="form-label">Ngày Trả:</label>
              <input type="date" id="ngayTraInput" name="ngayTraInput" class="form-control ${not empty errors.ngayTraInput ? 'is-invalid' : ''}"
                     value="${userInput.ngayTraInput}" required min="${minNgayTra}"> <%-- Sử dụng thuộc tính minNgayTra --%>
              <c:if test="${not empty errors.ngayTraInput}">
                <div class="invalid-feedback">
                  <c:out value="${errors.ngayTraInput}" />
                </div>
              </c:if>
              <div class="invalid-feedback">Vui lòng chọn ngày trả hợp lệ (sau hoặc bằng ngày mượn).</div>
            </div>

            <div class="d-grid gap-2 d-md-flex justify-content-md-center mt-4">
              <button type="submit" class="btn btn-success">
                <i class="bi bi-check-circle"></i> Mượn Sách
              </button>
              <button type="button" class="btn btn-secondary" id="backButton">
                <i class="bi bi-arrow-left-circle"></i> Trở về danh sách
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>

<script>
  const backButton = document.getElementById('backButton');
  if (backButton) {
    backButton.addEventListener('click', function() {
      if (confirm('Bạn có chắc chắn muốn trở về danh sách? Mọi thông tin chưa lưu sẽ bị mất.')) {
        window.location.href = '${pageContext.request.contextPath}/danh-sach-sach';
      }
    });
  }
</script>

</body>
</html>