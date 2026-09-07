package ni.edu.uam.facturacionapp.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class Cargo {
    private Integer id;
    private String nombre;
    private String descripcion;
}
