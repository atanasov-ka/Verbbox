import { BaseEntity } from './../../shared';

export class Verb implements BaseEntity {
    constructor(
        public id?: number,
        public front?: string,
        public back?: string,
        public created?: any,
        public box?: BaseEntity,
        public verbHistory?: BaseEntity,
    ) {
    }
}
