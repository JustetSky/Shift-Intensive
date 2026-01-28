package ru.shift.userimporter.core.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "file_processing_errors")
@Getter
@Setter
@NoArgsConstructor
public class FileProcessingError {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id", nullable = false)
    private UploadedFile file;

    @Column(name = "row_number", nullable = false)
    private Integer rowNumber;

    @Column(name = "error_message", nullable = false)
    private String errorMessage;

    @Enumerated(EnumType.STRING)
    @Column(name = "error_code", nullable = false)
    private ProcessingErrorCode errorCode;

    @Column(name = "raw_data")
    private String rawData;
}