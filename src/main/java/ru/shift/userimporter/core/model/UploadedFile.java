package ru.shift.userimporter.core.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@Entity
@Table(name = "uploaded_files")
@NoArgsConstructor
@AllArgsConstructor
public class UploadedFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "inserted_rows")
    private Integer insertedRows;

    @Column(name = "updated_rows")
    private Integer updatedRows;

    @Column(name = "original_filename", length = 50, nullable = false)
    private String originalFilename;

    @Column(name = "storage_path", length = 512, nullable = false)
    private String storagePath;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 50, nullable = false)
    private FileStatus fileStatus;
}