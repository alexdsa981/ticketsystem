let stompClient = null;
let socket = null;
let reconnectInterval = 5000;
let wasDisconnected = false;

function conectarDashboardWebSocket() {
  socket = new SockJS("/ws");
  stompClient = Stomp.over(socket);
  stompClient.debug = () => {};

  stompClient.connect(
    {},
    () => {
      if (wasDisconnected) {
        console.log("Reconectado. Actualizando dashboard...");
        const filtros = obtenerFiltrosActuales();
        fetchDatosDashboard(filtros);
        wasDisconnected = false;
      }

      stompClient.subscribe("/topic/estadoActual", function (mensaje) {
        if (mensaje.body === "actualizar") {
          const filtros = obtenerFiltrosActuales();
          console.log("🔄 WS pidió actualización con filtros:", filtros);
          fetchDatosDashboard(filtros);
        }
      });
    },
    (error) => {
      if (!wasDisconnected) {
        console.warn("WebSocket desconectado. Reintentando en 5 segundos...");
        wasDisconnected = true;
      }
      setTimeout(() => conectarDashboardWebSocket(), reconnectInterval);
    }
  );
}

document.addEventListener("DOMContentLoaded", () => {
  conectarDashboardWebSocket();
  fetchDatosDashboard(obtenerFiltrosActuales());
});
