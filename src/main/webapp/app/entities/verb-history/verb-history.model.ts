import { BaseEntity } from './../../shared';

export class VerbHistory implements BaseEntity {
    constructor(
        public id?: number,
        public frontBackYes?: number,
        public backFrontYes?: number,
        public frontBackNo?: number,
        public backFrontNo?: number,
        public user?: BaseEntity,
    ) {
    }
}
