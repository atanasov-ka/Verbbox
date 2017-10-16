import { BaseEntity, User } from './../../shared';

export class Box implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public firstLanguage?: string,
        public secondLanguage?: string,
        public description?: string,
        public created?: any,
        public verbs?: BaseEntity[],
        public plays?: BaseEntity[],
        public owner?: User,
    ) {
    }
}
