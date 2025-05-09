document.addEventListener('DOMContentLoaded', function () {
    function handleFormSubmit(formId) {
        const form = document.getElementById(formId);

        form.addEventListener('submit', function (event) {
            event.preventDefault(); // Evita que se envíe normalmente

            const url = form.getAttribute('action');
            const formData = new FormData(form);

            fetch(url, {
                method: 'POST',
                body: formData
            })
            .then(data => {
                // Aquí puedes mostrar un mensaje con SweetAlert si quieres
                // swal("Actualizado", "La información fue actualizada correctamente", "success")
                //     .then(() => location.reload());

                // O simplemente recargar la página
                location.reload();
            })
            .catch(error => {
                console.error('Error al actualizar:', error);
                alert('Ocurrió un error al enviar el formulario.');
            });
        });
    }

    handleFormSubmit('formNewCatIncidencia');
    handleFormSubmit('formNewSubCatIncidencia');
    handleFormSubmit('formNewTipoIncidencia');
});





//MODALES PARA ACTUALIZAR, OBTIENEN DATOS, LOS MUESTRAN EN MODAL Y ENVIAN LO COLOCADO EN LOS INPUT
function openCatIncidenciasModal(button) {
    var id = button.getAttribute("data-catIncidencias-id");
    var nombre = button.getAttribute("data-catIncidencias-nombre");

    document.getElementById("editCatIncidenciaId").value = id;
    document.getElementById("editCatIncidenciaNombre").value = nombre;

    const form = document.getElementById('editCatIncidenciaForm');
    const url = `/app/clasificadores/actualizar/catIncidencia/${id}`;
    form.setAttribute('action', url);

    form.onsubmit = function (event) {
        event.preventDefault();

        const formData = new FormData(form);

        fetch(url, {
            method: 'POST',
            body: formData
        })
        .then(() => location.reload())
        .catch(error => {
            console.error('Error al actualizar CatIncidencia:', error);
            alert('Ocurrió un error al actualizar la categoría.');
        });
    };
}

function openSubCatIncidenciaModal(button) {
    var id = button.getAttribute("data-subCatIncidencia-id");
    var nombre = button.getAttribute("data-subCatIncidencia-nombre");
    var id_cat = button.getAttribute("data-CatIncidencia-id");

    document.getElementById("subCatIncidenciaId").value = id;
    document.getElementById("subCatIncidenciaNombre").value = nombre;
    document.getElementById("editSubCatIncidenciaCat").value = id_cat;

    const form = document.getElementById('subCatIncidenciaForm');
    const url = `/app/clasificadores/actualizar/subCatIncidencia/${id}`;
    form.setAttribute('action', url);

    form.onsubmit = function (event) {
        event.preventDefault();

        const formData = new FormData(form);

        fetch(url, {
            method: 'POST',
            body: formData
        })
        .then(() => location.reload())
        .catch(error => {
            console.error('Error al actualizar SubCatIncidencia:', error);
            alert('Ocurrió un error al actualizar la subcategoría.');
        });
    };
}


function openIncidenciaModal(button) {
    var id = button.getAttribute("data-incidencia-id");
    var nombre = button.getAttribute("data-incidencia-nombre");
    var id_subcat = button.getAttribute("data-subCatIncidencia-id");

    document.getElementById("incidenciaId").value = id;
    document.getElementById("incidenciaNombre").value = nombre;
    document.getElementById("editTipoIncidenciaSubCat").value = id_subcat;

    const form = document.getElementById('incidenciaForm');
    const url = `/app/clasificadores/actualizar/tipoIncidencia/${id}`;
    form.setAttribute('action', url);

    form.onsubmit = function (event) {
        event.preventDefault();

        const formData = new FormData(form);

        fetch(url, {
            method: 'POST',
            body: formData
        })
        .then(() => location.reload())
        .catch(error => {
            console.error('Error al actualizar TipoIncidencia:', error);
            alert('Ocurrió un error al actualizar la incidencia.');
        });
    };
}


function openAtencionModal(button) {
    var id = button.getAttribute("data-atencion-id");
    var nombre = button.getAttribute("data-atencion-nombre");

    document.getElementById("atencionId").value = id;
    document.getElementById("atencionNombre").value = nombre;

    // Configurar la acción del formulario
    const form = document.getElementById('atencionForm');
    form.setAttribute('action', `/app/clasificadores/actualizar/atencion/${id}`);
}

function openAreaModal(button) {
    var id = button.getAttribute("data-area-id");
    var nombre = button.getAttribute("data-area-nombre");

    document.getElementById("areaId").value = id;
    document.getElementById("areaNombre").value = nombre;

    // Configurar la acción del formulario
    const form = document.getElementById('areaForm');
    form.setAttribute('action', `/app/clasificadores/actualizar/area/${id}`);
}


function openDesestimacionModal(button) {
    var id = button.getAttribute("data-desestimacion-id");
    var nombre = button.getAttribute("data-desestimacion-nombre");

    document.getElementById("desestimacionId").value = id;
    document.getElementById("desestimacionNombre").value = nombre;

    // Configurar la acción del formulario
    const form = document.getElementById('desestimacionForm');
    form.setAttribute('action', `/app/clasificadores/actualizar/desestimacion/${id}`);
}


