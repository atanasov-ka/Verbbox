import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { Verb } from './verb.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class VerbService {

    private resourceUrl = SERVER_API_URL + 'api/verbs';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(verb: Verb): Observable<Verb> {
        const copy = this.convert(verb);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(verb: Verb): Observable<Verb> {
        const copy = this.convert(verb);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<Verb> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        const result = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            result.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return new ResponseWrapper(res.headers, result, res.status);
    }

    /**
     * Convert a returned JSON object to Verb.
     */
    private convertItemFromServer(json: any): Verb {
        const entity: Verb = Object.assign(new Verb(), json);
        entity.created = this.dateUtils
            .convertLocalDateFromServer(json.created);
        return entity;
    }

    /**
     * Convert a Verb to a JSON which can be sent to the server.
     */
    private convert(verb: Verb): Verb {
        const copy: Verb = Object.assign({}, verb);
        copy.created = this.dateUtils
            .convertLocalDateToServer(verb.created);
        return copy;
    }
}
