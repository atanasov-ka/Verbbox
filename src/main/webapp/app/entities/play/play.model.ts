import { BaseEntity, User } from './../../shared';

export class Play implements BaseEntity {
    constructor(
        public id?: number,
        public progress?: number,
        public lastActiviry?: any,
        public box?: BaseEntity,
        public player?: User,
    ) {
    }
}