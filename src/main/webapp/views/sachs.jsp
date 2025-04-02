<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Danh Sách Sách</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <style>
        .table th, .table td { vertical-align: middle; }
    </style>
</head>
<body>

<div class="container mt-4">
    <h1 class="text-center mb-4">Danh Sách Sách Trong Thư Viện</h1>
    <c:choose>
        <c:when test="${not empty sachList}">
            <div class="table-responsive">
                <table class="table table-bordered table-striped table-hover">
                    <thead class="table-light">
                    <tr>
                        <th>Mã Sách</th>
                        <th>Tên Sách</th>
                        <th>Tác Giả</th>
                        <th>Mô Tả</th>
                        <th class="text-center">Số Lượng</th>
                        <th class="text-center">Hành động</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${sachList}" var="sach">
                        <tr>
                            <td><c:out value="${sach.maSach}" /></td>
                            <td><c:out value="${sach.tenSach}" /></td>
                            <td><c:out value="${sach.tacGia}" /></td>
                            <td><c:out value="${sach.moTa}" /></td>
                            <td class="text-center"><c:out value="${sach.soLuong}" /></td>
                            <td class="text-center">
                                    <%-- SỬA LỖI Ở ĐÂY: Bỏ logic disable nút --%>
                                <a href="${pageContext.request.contextPath}/muon-sach?maSach=${sach.maSach}"
                                   class="btn btn-primary btn-sm">
                                    Mượn Sách
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:when>
        <c:otherwise>
            <div class="alert alert-info text-center" role="alert">
                Không có sách nào trong cơ sở dữ liệu.
            </div>
        </c:otherwise>
    </c:choose>

    <div class="modal fade" id="infoModal" tabindex="-1" aria-labelledby="infoModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="infoModalLabel">Thông báo</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body" id="modalMessageBody">
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
                </div>
            </div>
        </div>
    </div>

</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>

<script>
    // JavaScript cho modal (giữ nguyên như trước)
    document.addEventListener('DOMContentLoaded', function() {
        const infoModalElement = document.getElementById('infoModal');
        if(infoModalElement) {
            const infoModal = new bootstrap.Modal(infoModalElement);
            const modalMessageBody = document.getElementById('modalMessageBody');
            const modalTitle = document.getElementById('infoModalLabel');

            let messageToShow = null;
            let titleToShow = "Thông báo";
            let titleClass = '';

            <c:choose>
            <c:when test="${not empty param.errorHetSach and not empty param.tenSach}">
            messageToShow = "Sách \"<strong><c:out value='${param.tenSach}'/></strong>\" đã hết hàng, không thể mượn.";
            titleToShow = "Không thể mượn";
            titleClass = 'text-danger';
            </c:when>
            <c:when test="${not empty param.error}">
            messageToShow = "Lỗi: <c:out value='${param.error}'/>";
            titleToShow = "Lỗi";
            titleClass = 'text-danger';
            </c:when>
            <c:when test="${not empty param.success}">
            messageToShow = "Thao tác mượn sách thành công!";
            titleToShow = "Thành công";
            titleClass = 'text-success';
            </c:when>
            </c:choose>

            if (messageToShow && modalMessageBody) {
                modalMessageBody.innerHTML = messageToShow;
                modalTitle.textContent = titleToShow;
                modalTitle.classList.remove('text-danger', 'text-success');
                if(titleClass) {
                    modalTitle.classList.add(titleClass);
                }
                infoModal.show();
            }

            infoModalElement.addEventListener('hidden.bs.modal', event => {
                modalTitle.classList.remove('text-danger', 'text-success');
            });
        } else {
            console.error("Bootstrap Modal element #infoModal not found.");
        }
    });
</script>

</body>
</html>