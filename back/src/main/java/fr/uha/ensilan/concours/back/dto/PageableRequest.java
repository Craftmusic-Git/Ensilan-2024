package fr.uha.ensilan.concours.back.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Objects;
import java.util.Optional;

@Getter
@Setter
public class PageableRequest {
    private String query;
    private Integer page;
    private Integer size;
    private String sort;

    public Pageable getPageable() {
        var pageable = Pageable.unpaged();

        if (Objects.nonNull(this.sort)) {
            pageable = Pageable.unpaged(Sort.by(this.sort));
        }

        if (Objects.nonNull(this.size) || Objects.nonNull(this.page)) {
            var pageNb = Optional.ofNullable(this.page).orElse(0);
            var sizeNb = Optional.ofNullable(this.size).orElse(10);

            if (Objects.nonNull(this.sort)) {
                pageable = PageRequest.of(pageNb, sizeNb, Sort.by(this.sort));
            } else {
                pageable = PageRequest.of(pageNb, sizeNb);
            }
        }

        return pageable;
    }

}
