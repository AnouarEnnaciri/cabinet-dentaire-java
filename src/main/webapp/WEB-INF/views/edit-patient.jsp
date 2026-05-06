<form method="post" action="${pageContext.request.contextPath}/patients/edit">

    <input type="hidden" name="id" value="<%= p.getId() %>"/>

    <input type="text" name="name" value="<%= p.getName() %>" />
    <input type="text" name="phone" value="<%= p.getPhone() %>" />
    <input type="email" name="email" value="<%= p.getEmail() %>" />
    <input type="number" name="age" value="<%= p.getAge() %>" />

    <button type="submit">Enregistrer</button>

</form>