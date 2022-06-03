struct SSeq;
class STypeSupport;
class SDataWriter;
class SDataReader;

class S
{
    public:
        typedef struct SSeq Seq;
        typedef STypeSupport TypeSupport;
        typedef SDataWriter DataWriter;
        typedef SDataReader DataReader;

        CDR_Short a=1,b=1;

        CDR_Long c=2,e=2;

        CDR_LongLong g=20,h=567;

        CDR_UnsignedLong i=10;

        CDR_UnsignedLongLong j=9;

        CDR_Char k='a',m='d';

        CDR_String n="2019302708";

        CDR_Boolean p=true,q=false;

        CDR_Float r=1.1;

        CDR_Double s=5.5;

        CDR_LongDouble t=5.6666;

        CDR_Float u[3]={1.1,2.2,3.3};
};

extern const char *STYPENAME;

REDA_DEFINE_SEQUENCE_STRUCT(SSeq, S);

REDA_DEFINE_SEQUENCE_IN_C(SSeq, S);

NDDSUSERDllExport extern RTI_BOOL
S_initialize(S* sample)
{
    CDR_Primitive_init_Short(&sample->a);
    CDR_Primitive_init_Short(&sample->b);
    CDR_Primitive_init_Long(&sample->c);
    CDR_Primitive_init_Long(&sample->e);
    CDR_Primitive_init_LongLong(&sample->g);
    CDR_Primitive_init_LongLong(&sample->h);
    CDR_Primitive_init_UnsignedLong(&sample->i);
    CDR_Primitive_init_UnsignedLongLong(&sample->j);
    CDR_Primitive_init_Char(&sample->k);
    CDR_Primitive_init_Char(&sample->m);
    if (!CDR_Primitive_initialize(&sample->n, (255)))
    {
         return RTI_FALSE;
    }

    CDR_Primitive_init_Boolean(&sample->p);
    CDR_Primitive_init_Boolean(&sample->q);
    CDR_Primitive_init_Float(&sample->r);
    CDR_Primitive_init_Double(&sample->s);
    CDR_Primitive_init_LongDouble(&sample->t);
    CDR_Primitive_init_Array(
        sample->u, ((3)*CDR_u_SIZE));

    return RTI_TRUE;
}

NDDSUSERDllExport extern RTI_BOOL
S_finalize(S* sample)
{
    UNUSED_ARG(sample);
    CDR_String_finalize(&sample->n);
    RTI_UINT32 i;

    for (i = 0; i < 3; i++) {
        if (!CDR_Float_copy(&dst-?u[i] ,
        &src->u[i])) {
        return RTI_FALSE;
        }
    }
    return RTI_TRUE;
}