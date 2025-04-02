<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Thống kê sách đang mượn</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <style>
        .table th, .table td { vertical-align: middle; }
    </style>
</head>
<body>
<div class="container mt-4">
    <h1 class="text-center mb-4">Thống kê sách đang cho mượn</h1>

    <form method="GET" action="${pageContext.request.contextPath}/thong-ke-muon-sach" class="row g-3 align-items-end bg-light p-3 rounded border mb-4">
        <div class="col-md-4">
            <label for="tenSachSearch" class="form-label">Tên sách:</label>
            <input type="text" class="form-control form-control-sm" id="tenSachSearch" name="tenSachSearch" value="<c:out value='${tenSachSearch}'/>">
        </div>
        <div class="col-md-4">
            <label for="tenHocSinhSearch" class="form-label">Tên học sinh:</label>
            <input type="text" class="form-control form-control-sm" id="tenHocSinhSearch" name="tenHocSinhSearch" value="<c:out value='${tenHocSinhSearch}'/>">
        </div>
        <div class="col-md-auto">
            <button type="submit" class="btn btn-primary btn-sm w-100">Tìm kiếm</button>
        </div>
    </form>

    <c:if test="${not empty param.returnError}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            Lỗi khi trả sách: <c:out value="${param.returnError}"/>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>
    <c:if test="${not empty param.returnSuccess}">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            Đã cập nhật trạng thái trả sách thành công!
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>

    <c:choose>
        <c:when test="${not empty loanList}">
            <div class="table-responsive">
                <table class="table table-bordered table-striped table-hover">
                    <thead class="table-light">
                    <tr>
                        <th>Mã mượn sách</th>
                        <th>Tên sách</th>
                        <th>Tác giả</th>
                        <th>Tên học sinh</th>
                        <th>Lớp</th>
                        <th>Ngày mượn</th>
                        <th>Ngày trả (dự kiến)</th>
                        <th class="text-center">Hành động</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${loanList}" var="loan">
                        <tr>
                            <td>MS-<fmt:formatNumber value="${loan.maMuonSach}" pattern="0000" /></td>
                            <td><c:out value="${loan.tenSach}" /></td>
                            <td><c:out value="${loan.tacGia}" /></td>
                            <td><c:out value="${loan.hoTenHocSinh}" /></td>
                            <td><c:out value="${loan.lopHocSinh}" /></td>
                            <td><fmt:formatDate value="${loan.ngayMuon}" pattern="dd/MM/yyyy" /></td>
                            <td><fmt:formatDate value="${loan.ngayTra}" pattern="dd/MM/yyyy" /></td>
                            <td class="text-center">
                                <button type="button" class="btn btn-danger btn-sm btn-show-confirm"
                                        data-bs-toggle="modal"
                                        data-bs-target="#confirmReturnModal"
                                        data-mamuonsach="${loan.maMuonSach}"
                                        data-masach="${loan.maSach}"
                                        data-tensach="<c:out value='${loan.tenSach}' escapeXml='true'/>"
                                        data-tenhocsinh="<c:out value='${loan.hoTenHocSinh}' escapeXml='true'/>">
                                    Trả sách
                                </button>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:when>
        <c:otherwise>
            <div class="alert alert-info text-center" role="alert">
                Không tìm thấy lượt mượn sách nào đang hoạt động khớp với tiêu chí.
            </div>
        </c:otherwise>
    </c:choose>

    <form id="hiddenReturnForm" method="POST" action="${pageContext.request.contextPath}/tra-sach" style="display: none;">
        <input type="hidden" name="maMuonSach" id="hiddenMaMuonSach">
        <input type="hidden" name="maSach" id="hiddenMaSach">
    </form>

    <div class="modal fade" id="confirmReturnModal" tabindex="-1" aria-labelledby="confirmReturnModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="confirmReturnModalLabel">Xác nhận trả sách</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body" id="confirmReturnMessage">
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy bỏ</button>
                    <button type="button" class="btn btn-primary" id="modalConfirmReturnButton">Xác nhận trả</button>
                </div>
            </div>
        </div>
    </div>

</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>

<script>
    const confirmReturnModalElement = document.getElementById('confirmReturnModal');
    const confirmReturnModal = confirmReturnModalElement ? new bootstrap.Modal(confirmReturnModalElement) : null;
    const confirmMessageEl = document.getElementById('confirmReturnMessage');
    const hiddenReturnForm = document.getElementById('hiddenReturnForm');
    const hiddenMaMuonSachInput = document.getElementById('hiddenMaMuonSach');
    const hiddenMaSachInput = document.getElementById('hiddenMaSach');
    const modalConfirmBtn = document.getElementById('modalConfirmReturnButton');

    if (confirmReturnModalElement) {
        confirmReturnModalElement.addEventListener('show.bs.modal', function (event) {
            const button = event.relatedTarget;
            const maMuonSach = button.getAttribute('data-mamuonsach');
            const maSach = button.getAttribute('data-masach');
            const tenSach = button.getAttribute('data-tensach');
            const tenHocSinh = button.getAttribute('data-tenhocsinh');

            if (confirmMessageEl) {
                confirmMessageEl.innerHTML = "Bạn có chắc chắn muốn trả sách \"<strong>" + tenSach + "</strong>\" <br>do học sinh <strong>" + tenHocSinh + "</strong> mượn không?";
            }
            if (hiddenMaMuonSachInput && hiddenMaSachInput) {
                hiddenMaMuonSachInput.value = maMuonSach;
                hiddenMaSachInput.value = maSach;
            }
        });

        confirmReturnModalElement.addEventListener('hidden.bs.modal', function (event) {
            if (hiddenMaMuonSachInput) hiddenMaMuonSachInput.value = '';
            if (hiddenMaSachInput) hiddenMaSachInput.value = '';
        });
    }

    if (modalConfirmBtn) {
        modalConfirmBtn.addEventListener('click', function() {
            if (hiddenReturnForm && hiddenMaMuonSachInput.value) {
                hiddenReturnForm.submit();
                if(confirmReturnModal) {
                    confirmReturnModal.hide();
                }
            } else {
                console.error("Hidden return form not found or MaMuonSach is empty.");
            }
        });
    }
</script>
</body>
</html>