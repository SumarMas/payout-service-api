package com.platform.payout_service.map;

/** Generic mapper interface for mapping entities to DTOs,
 * and vice versa.
 *
 * @param <T> the type of the DTO
 * @param <E> the type of the entity
 */
public interface IMapper<T, E> {
    /** Maps an entity to a DTO.
     *
     * @param entity the entity to map
     * @return the mapped DTO
     */
    T mapToDto(E entity);
}
