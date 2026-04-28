SELECT DISTINCT c.id_cliente, c.documento, c.nombre, c.email
FROM btg.cliente c
JOIN btg.inscripcion i
  ON i.id_cliente = c.id_cliente
JOIN btg.visitan v
  ON v.id_cliente = c.id_cliente
JOIN btg.disponibilidad d
  ON d.id_producto = i.id_producto
 AND d.id_sucursal = v.id_sucursal
WHERE d.disponible = TRUE;
